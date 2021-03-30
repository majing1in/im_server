package com.xiaoma.im.manager.user;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.UserInformationMapper;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.entity.UserInformation;
import com.xiaoma.im.manager.HandlerBusiness;
import com.xiaoma.im.utils.DateUtils;
import com.xiaoma.im.utils.NettyAttrUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

/**
 * @Author Xiaoma
 * @Date 2021/2/15 0015 21:27
 * @Email 1468835254@qq.com
 */
@Slf4j
@Service(value = "user-info-update")
public class PersonalDetailUpdateImpl implements HandlerBusiness {

    @Resource
    private UserInformationMapper userInformationMapper;

    @Resource
    private PlatformTransactionManager transactionManager;

    @Override
    public void process(byte[] content, ChannelHandlerContext channelHandlerContext) {
        UserInformation updateUserInformation = ObjectUtil.deserialize(content);
        log.info("当前用户 {} 正在修改用户信息...", updateUserInformation.getUserAccount());
        updateUserInformation.setUpdateTime(DateUtils.localDateTimeConvertToDate());
        MessagePackage messagePackage = null;
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            userInformationMapper.update(updateUserInformation, new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUserAccount, updateUserInformation.getUserAccount()));
            transactionManager.commit(status);
            messagePackage = MessagePackage.completePackage(Constants.ME_INFO_UPDATE, ObjectUtil.serialize(Constants.SUCCESS));
        } catch (Exception e) {
            transactionManager.rollback(status);
            messagePackage = MessagePackage.completePackage(Constants.ME_INFO_UPDATE, ObjectUtil.serialize(Constants.FAILED));
        } finally {
            NettyAttrUtil.isSendSuccess(channelHandlerContext, messagePackage);
            log.info("当前用户 {} 修改用户信息结束 修改信息 = {}", updateUserInformation.getUserAccount(), JSON.toJSONString(updateUserInformation));
        }
    }
}
