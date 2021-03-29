package com.xiaoma.im.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.MessageGroupUsersMapper;
import com.xiaoma.im.dao.PointToPointMapper;
import com.xiaoma.im.dao.UserInfoMapper;
import com.xiaoma.im.dao.UserMoneyMapper;
import com.xiaoma.im.entity.*;
import com.xiaoma.im.enums.CommandType;
import com.xiaoma.im.service.PackageService;
import com.xiaoma.im.service.UserInfoService;
import com.xiaoma.im.utils.RedisTemplateUtils;
import com.xiaoma.im.utils.UuidUtils;
import com.xiaoma.im.vo.GroupChatVo;
import com.xiaoma.im.vo.PointToPointVo;
import org.springframework.stereotype.Service;
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
    private RedisTemplateUtils redisTemplateUtils;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteMoney(UserMoney userMoney, BigDecimal bigDecimal) {
        userMoney.setMoney(bigDecimal);
        UpdateWrapper<UserMoney> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(UserMoney::getUid, userMoney.getUid());
        return userMoneyMapper.update(userMoney, wrapper) == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean generatorSinglePackage(RedPackage redPackage, Integer id) {
        UserInfo sender = userInfoService.getUserInfoServiceByAccount(redPackage.getRedPackageOwner());
        UserInfo receiver = userInfoService.getUserInfoServiceById(id);
        String redPackageId = UuidUtils.getUuid();
        PointToPointVo pointToPointVo = new PointToPointVo();
        pointToPointVo.setCreateTime(redPackage.getCreateTime());
        pointToPointVo.setCommandType(Constants.RED_PACKAGE_ONE);
        pointToPointVo.setMessageContent(redPackageId);
        pointToPointVo.setSenderId(sender.getId());
        pointToPointVo.setReceiverId(receiver.getId());
        // 插入到历史消息列表
        pointToPointMapper.insert(pointToPointVo);
        if (redisTemplateUtils.setIfAbsent(Constants.RED_PACKAGE_ONE_KEY + redPackageId, redPackage.getCount(), 1, TimeUnit.DAYS)) {
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean generatorGroupPackage(RedPackage redPackage, Integer id) {
        UserInfo sender = userInfoService.getUserInfoServiceByAccount(redPackage.getRedPackageOwner());
        // 获取所有用户id
        List<MessageGroupUsers> groupUsers = groupUsersMapper.selectList(new LambdaUpdateWrapper<MessageGroupUsers>().eq(MessageGroupUsers::getGroupId, id));
        List<Integer> userIds = groupUsers.stream().map(MessageGroupUsers::getUserId).collect(Collectors.toList());
        List<UserInfo> userInfos = userInfoMapper.selectBatchIds(userIds);
        String redPackageId = UuidUtils.getUuid();
        GroupChatVo chatVo = new GroupChatVo();
        chatVo.setUserNickName(sender.getUserNickName());
        chatVo.setMessageType(Constants.RED_PACKAGE_GROUP);
        chatVo.setGroupId(id);
        chatVo.setMessageContent(redPackageId);
        chatVo.setCreateTime(redPackage.getCreateTime());
        for (UserInfo userInfo : userInfos) {
            UserStatus userStatus = (UserStatus) redisTemplateUtils.getHashKey(Constants.SERVER_REDIS_LIST, Constants.SERVER_ONLINE + userInfo.getUserAccount());
            MessagePackage messagePackage = MessagePackage.completePackage(Constants.GROUP_LIST_MESSAGE, ObjectUtil.serialize(chatVo));
            if (ObjectUtil.isNull(userStatus)) {
                redisTemplateUtils.rightPush(Constants.SERVER_USER_ACCOUNT + userInfo.getUserAccount(), messagePackage);
                continue;
            }
            userStatus.getChannel().writeAndFlush(messagePackage);
        }
        return false;
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

}
