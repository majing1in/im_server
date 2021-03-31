package com.xiaoma.im.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.*;
import com.xiaoma.im.entity.*;
import com.xiaoma.im.enums.CommandType;
import com.xiaoma.im.service.PackageService;
import com.xiaoma.im.service.UserInfoService;
import com.xiaoma.im.utils.RedisTemplateUtils;
import com.xiaoma.im.utils.SqlUtils;
import com.xiaoma.im.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Slf4j
@Service
public class PackageServiceImpl implements PackageService {

    public static final String REDIS_LOCK = "redis_lock";

    @Resource
    private PrivateChatListMapper privateChatListMapper;

    @Resource
    private UserBalanceMoneyMapper userBalanceMoneyMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserInformationMapper userInformationMapper;

    @Resource
    private GroupFriendsMapper groupUsersMapper;

    @Resource
    private GroupChatListMapper groupChatMapper;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private PlatformTransactionManager platformTransactionManager;

    @Override
    public boolean updateMoney(UserBalanceMoney userBalanceMoney, BigDecimal bigDecimal) {
        userBalanceMoney.setMoney(bigDecimal);
        UpdateWrapper<UserBalanceMoney> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(UserBalanceMoney::getUid, userBalanceMoney.getUid());
        return userBalanceMoneyMapper.update(userBalanceMoney, wrapper) == 1;
    }

    @Override
    public boolean generatorSinglePackage(RedPackage redPackage, Integer id) {
        UserInformation sender = userInfoService.getUserInfoServiceByAccount(redPackage.getRedPackageOwner());
        UserInformation receiver = userInfoService.getUserInfoServiceById(id);
        String redPackageId = UuidUtils.getUuid();
        PrivateChatList messageQueueList = new PrivateChatList();
        messageQueueList.setCreateTime(redPackage.getCreateTime());
        messageQueueList.setMessageType(Constants.RED_PACKAGE_ONE);
        messageQueueList.setMessageContent(redPackageId);
        messageQueueList.setSenderId(sender.getId());
        messageQueueList.setReceiverId(receiver.getId());
        privateChatListMapper.insert(messageQueueList);
        if (redisTemplateUtils.setIfAbsent(Constants.RED_PACKAGE_ONE_KEY + redPackageId, redPackage, 1, TimeUnit.DAYS)) {
            UserStatus userStatus = (UserStatus) redisTemplateUtils.getHashKey(Constants.SERVER_REDIS_LIST, Constants.SERVER_ONLINE + receiver.getUserAccount());
            MessagePackage messagePackage = MessagePackage.completePackage(CommandType.COMMAND_PACKAGE.getCode(), ObjectUtil.serialize(redPackageId));
            if (ObjectUtil.isNull(userStatus)) {
                redisTemplateUtils.rightPush(Constants.SERVER_USER_ACCOUNT + receiver.getUserAccount(), messagePackage);
            } else {
                userStatus.getChannel().writeAndFlush(messagePackage);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean generatorGroupPackage(RedPackage redPackage, Integer id) {
        UserInformation sender = userInfoService.getUserInfoServiceByAccount(redPackage.getRedPackageOwner());
        List<GroupFriends> groupUsers = groupUsersMapper.selectList(new LambdaUpdateWrapper<GroupFriends>().eq(GroupFriends::getGroupId, id));
        List<Integer> userIds = groupUsers.stream().map(GroupFriends::getUserId).collect(Collectors.toList());
        List<UserInformation> userInformationList = userInformationMapper.selectBatchIds(userIds);
        String redPackageId = UuidUtils.getUuid();
        GroupChatList groupChatList = new GroupChatList();
        groupChatList.setSenderId(sender.getId());
        groupChatList.setMessageType(Constants.RED_PACKAGE_GROUP);
        groupChatList.setGroupId(id);
        groupChatList.setMessageContent(redPackageId);
        groupChatList.setCreateTime(redPackage.getCreateTime());
        groupChatMapper.insert(groupChatList);
        if (!redisTemplateUtils.setIfAbsent(Constants.RED_PACKAGE_GROUP_KEY + redPackageId, redPackage, 2, TimeUnit.DAYS)) {
            return false;
        }
        for (UserInformation userInformation : userInformationList) {
            UserStatus userStatus = (UserStatus) redisTemplateUtils.getHashKey(Constants.SERVER_REDIS_LIST, Constants.SERVER_ONLINE + userInformation.getUserAccount());
            MessagePackage messagePackage = MessagePackage.completePackage(Constants.GROUP_LIST_MESSAGE, ObjectUtil.serialize(groupChatList));
            if (ObjectUtil.isNull(userStatus)) {
                redisTemplateUtils.rightPush(Constants.SERVER_USER_ACCOUNT + userInformation.getUserAccount(), messagePackage);
                continue;
            }
            userStatus.getChannel().writeAndFlush(messagePackage);
        }
        return true;
    }

    @Override
    public Integer verificationData(UserBalanceMoney userBalanceMoney, RedPackage redPackage) {
        if (userBalanceMoney.getMoney().compareTo(redPackage.getAmount()) < 0) {
            return 1;
        }
        BigDecimal averageMoney = userBalanceMoney.getMoney().divide(new BigDecimal(redPackage.getCount()), 2, RoundingMode.HALF_UP);
        if (averageMoney.compareTo(new BigDecimal("0.01")) < 0) {
            return 2;
        }
        return 0;
    }

    @Override
    public boolean getSinglePackage(String redPackageId, String account) {
        TransactionStatus transaction = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            RedPackage redPackage = (RedPackage) redisTemplateUtils.get(Constants.RED_PACKAGE_ONE_KEY + redPackageId);
            if (ObjectUtil.isNull(redPackage)) {
                return false;
            }
            UserBalanceMoney userBalanceMoney = userBalanceMoneyMapper.getUserMoney(SqlUtils.buildSqlForUserMoney(account));
            BigDecimal bigDecimal = userBalanceMoney.getMoney().add(redPackage.getAmount());
            if (this.updateMoney(userBalanceMoney, bigDecimal)) {
                redisTemplateUtils.delete(Constants.RED_PACKAGE_ONE_KEY + redPackageId);
                platformTransactionManager.commit(transaction);
                log.info("用户 {} 获取红包成功！, redPackageId = {}", account, redPackageId);
                return true;
            } else {
                log.info("用户 {} 获取红包失败！, redPackageId = {}", account, redPackageId);
                throw new RuntimeException();
            }
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public boolean getGroupPackage(String redPackageId, String account) {
        if (!redisTemplateUtils.hasKey(Constants.RED_PACKAGE_GROUP_KEY + redPackageId)) {
            log.info("红包 {} 不存在直接返回...", redPackageId);
            return false;
        }
        RLock rLock = redissonClient.getLock(REDIS_LOCK + redPackageId);
        try {
            rLock.lock();
            log.info("用户 {} 获取锁开始抢红包操作...", account);
            RedPackage redPackage = (RedPackage) redisTemplateUtils.get(Constants.RED_PACKAGE_GROUP_KEY + redPackageId);
            UserBalanceMoney userBalanceMoney = userBalanceMoneyMapper.getUserMoney(SqlUtils.buildSqlForUserMoney(account));
            if (redPackage.getList().contains(userBalanceMoney.getUid())) {
                return false;
            }
            if (redPackage.getCount() == 1) {
                BigDecimal addUp = userBalanceMoney.getMoney().add(redPackage.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
                redisTemplateUtils.delete(Constants.RED_PACKAGE_GROUP_KEY + redPackageId);
                return this.updateMoney(userBalanceMoney, addUp);
            }
            BigDecimal minDecimal = redPackage.getAmount().divide(new BigDecimal(redPackage.getCount()), 2, RoundingMode.HALF_UP).divide(new BigDecimal(5), 2, RoundingMode.HALF_UP);
            BigDecimal maxDecimal = redPackage.getAmount().multiply(new BigDecimal("0.6")).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal money = BigDecimal.valueOf(minDecimal.doubleValue() + Math.random() * maxDecimal.doubleValue() % (maxDecimal.doubleValue() - minDecimal.doubleValue() + 1)).add(new BigDecimal(0)).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (ObjectUtil.isNull(userBalanceMoney)) {
                UserInformation userInformation = userInfoService.getUserInfoServiceByAccount(account);
                userBalanceMoney = new UserBalanceMoney();
                userBalanceMoney.setMoney(new BigDecimal(0));
                userBalanceMoney.setUid(userInformation.getId());
                userBalanceMoney.setVersion(0);
                userBalanceMoneyMapper.insert(userBalanceMoney);
            }
            BigDecimal addUp = userBalanceMoney.getMoney().add(money).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (!this.updateMoney(userBalanceMoney, addUp)) {
                return false;
            }
            redPackage.getList().add(userBalanceMoney.getUid());
            BigDecimal remaining = redPackage.getAmount().subtract(money).setScale(2, BigDecimal.ROUND_HALF_UP);
            redPackage.setAmount(remaining);
            redPackage.setCount(redPackage.getCount() - 1);
            redisTemplateUtils.set(Constants.RED_PACKAGE_GROUP_KEY + redPackageId, redPackage);
            log.info("用户 {} 获取锁抢红包操作完成...", account);
            return true;
        } finally {
            // 是否还是锁定状态
            if (rLock.isLocked()) {
                if (rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                    log.info("用户 {} 用户抢红包释放锁资源...", account);
                }
            }
        }
    }

}
