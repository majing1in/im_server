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
import com.xiaoma.im.utils.UuidUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Service
public class PackageServiceImpl implements PackageService {

    public static final String REDIS_LOCK = "redis_lock";

    @Resource
    private PointToPointMapper pointToPointMapper;

    @Resource
    private UserMoneyMapper userMoneyMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private MessageGroupUsersMapper groupUsersMapper;

    @Resource
    private MessageGroupChatMapper groupChatMapper;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public boolean updateMoney(UserMoney userMoney, BigDecimal bigDecimal) {
        userMoney.setMoney(bigDecimal);
        UpdateWrapper<UserMoney> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(UserMoney::getUid, userMoney.getUid());
        return userMoneyMapper.update(userMoney, wrapper) == 1;
    }

    @Override
    public boolean generatorSinglePackage(RedPackage redPackage, Integer id) {
        UserInfo sender = userInfoService.getUserInfoServiceByAccount(redPackage.getRedPackageOwner());
        UserInfo receiver = userInfoService.getUserInfoServiceById(id);
        String redPackageId = UuidUtils.getUuid();
        PointToPoint pointToPoint = new PointToPoint();
        pointToPoint.setCreateTime(redPackage.getCreateTime());
        pointToPoint.setMessageType(Constants.RED_PACKAGE_ONE);
        pointToPoint.setMessageContent(redPackageId);
        pointToPoint.setSenderId(sender.getId());
        pointToPoint.setReceiverId(receiver.getId());
        // 插入到历史消息列表
        pointToPointMapper.insert(pointToPoint);
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
        UserInfo sender = userInfoService.getUserInfoServiceByAccount(redPackage.getRedPackageOwner());
        // 获取所有用户id
        List<MessageGroupUsers> groupUsers = groupUsersMapper.selectList(new LambdaUpdateWrapper<MessageGroupUsers>().eq(MessageGroupUsers::getGroupId, id));
        List<Integer> userIds = groupUsers.stream().map(MessageGroupUsers::getUserId).collect(Collectors.toList());
        List<UserInfo> userInfos = userInfoMapper.selectBatchIds(userIds);
        String redPackageId = UuidUtils.getUuid();
        MessageGroupChat messageGroupChat = new MessageGroupChat();
        messageGroupChat.setSenderId(sender.getId());
        messageGroupChat.setMessageType(Constants.RED_PACKAGE_GROUP);
        messageGroupChat.setGroupId(id);
        messageGroupChat.setMessageContent(redPackageId);
        messageGroupChat.setCreateTime(redPackage.getCreateTime());
        groupChatMapper.insert(messageGroupChat);
        if (!redisTemplateUtils.setIfAbsent(Constants.RED_PACKAGE_GROUP_KEY + redPackageId, redPackage, 2, TimeUnit.DAYS)) {
            return false;
        }
        for (UserInfo userInfo : userInfos) {
            UserStatus userStatus = (UserStatus) redisTemplateUtils.getHashKey(Constants.SERVER_REDIS_LIST, Constants.SERVER_ONLINE + userInfo.getUserAccount());
            MessagePackage messagePackage = MessagePackage.completePackage(Constants.GROUP_LIST_MESSAGE, ObjectUtil.serialize(messageGroupChat));
            if (ObjectUtil.isNull(userStatus)) {
                redisTemplateUtils.rightPush(Constants.SERVER_USER_ACCOUNT + userInfo.getUserAccount(), messagePackage);
                continue;
            }
            userStatus.getChannel().writeAndFlush(messagePackage);
        }
        return true;
    }

    @Override
    public Integer verificationData(UserMoney userMoney, RedPackage redPackage) {
        if (userMoney.getMoney().compareTo(redPackage.getAmount()) < 0) {
            return 1;
        }
        BigDecimal averageMoney = userMoney.getMoney().divide(new BigDecimal(redPackage.getCount()), 2, RoundingMode.HALF_UP);
        if (averageMoney.compareTo(new BigDecimal("0.01")) < 0) {
            return 2;
        }
        return 0;
    }

    @Override
    public boolean getSinglePackage(String redPackageId, String account) {
        RedPackage redPackage = (RedPackage) redisTemplateUtils.get(Constants.RED_PACKAGE_ONE_KEY + redPackageId);
        if (ObjectUtil.isNull(redPackage)) {
            return false;
        }
        UserMoney userMoney = userMoneyMapper.getUserMoney(this.buildSqlForUserMoney(account));
        BigDecimal bigDecimal = userMoney.getMoney().add(redPackage.getAmount());
        if (this.updateMoney(userMoney, bigDecimal)) {
            redisTemplateUtils.delete(Constants.RED_PACKAGE_ONE_KEY + redPackageId);
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public boolean getGroupPackage(String redPackageId, String account) {
        RLock rLock = redissonClient.getLock(REDIS_LOCK);
        try {
            rLock.lock();
            RedPackage redPackage = (RedPackage) redisTemplateUtils.get(Constants.RED_PACKAGE_GROUP_KEY + redPackageId);
            UserMoney userMoney = userMoneyMapper.getUserMoney(this.buildSqlForUserMoney(account));
            if (redPackage.getList().contains(userMoney.getUid())) {
                return false;
            }
            if (redPackage.getCount() == 1) {
                BigDecimal addUp = userMoney.getMoney().add(redPackage.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
                return this.updateMoney(userMoney, addUp);
            }
            BigDecimal minDecimal = redPackage.getAmount().divide(new BigDecimal(redPackage.getCount()), 2, RoundingMode.HALF_UP).divide(new BigDecimal(5), 2, RoundingMode.HALF_UP);
            BigDecimal maxDecimal = redPackage.getAmount().multiply(new BigDecimal("0.6")).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal money = BigDecimal.valueOf(minDecimal.doubleValue() + Math.random() * maxDecimal.doubleValue() % (maxDecimal.doubleValue() - minDecimal.doubleValue() + 1)).add(new BigDecimal(0)).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (ObjectUtil.isNull(userMoney)) {
                UserInfo userInfo = userInfoService.getUserInfoServiceByAccount(account);
                userMoney = new UserMoney();
                userMoney.setMoney(new BigDecimal(0));
                userMoney.setUid(userInfo.getId());
                userMoney.setVersion(0);
                userMoneyMapper.insert(userMoney);
            }
            BigDecimal addUp = userMoney.getMoney().add(money).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (!this.updateMoney(userMoney, addUp)) {
                return false;
            }
            redPackage.getList().add(userMoney.getUid());
            BigDecimal remaining = redPackage.getAmount().subtract(money).setScale(2, BigDecimal.ROUND_HALF_UP);
            redPackage.setAmount(remaining);
            redPackage.setCount(redPackage.getCount() - 1);
            redisTemplateUtils.set(Constants.RED_PACKAGE_GROUP_KEY + redPackageId, redPackage);
            return true;
        } finally {
            // 是否还是锁定状态
            if (rLock.isLocked()) {
                if (rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }
        }
    }

    private String buildSqlForUserMoney(String account) {
        return "SELECT * from user_money WHERE uid = (SELECT id from user_info WHERE user_account ='" + account + "')";
    }

}
