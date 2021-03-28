package com.xiaoma.im.manager.message;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.PointToPointMapper;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.enums.MessageTypeEnum;
import com.xiaoma.im.enums.ResponseEnum;
import com.xiaoma.im.manager.HandlerBusiness;
import com.xiaoma.im.utils.RedisTemplateUtils;
import com.xiaoma.im.utils.SessionSocketUtils;
import com.xiaoma.im.vo.PointToPointVo;
import com.xiaoma.im.vo.ResponseMessageVo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * @Author Xiaoma
 * @Date 2021/2/16 0016 12:10
 * @Email 1468835254@qq.com
 */
@Slf4j
@Service(value = "send-single")
public class SinglePointChatImpl implements HandlerBusiness {

    @Resource
    private PointToPointMapper pointToPointMapper;

    @Resource
    private SessionSocketUtils sessionSocketUtils;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Resource
    private PlatformTransactionManager transactionManager;

    @Override
    public void process(byte[] content, ChannelHandlerContext channelHandlerContext) {
        PointToPointVo messageSingle = ObjectUtil.deserialize(content);
        NioSocketChannel channel = sessionSocketUtils.getUserNioSocketChannelByAccount(messageSingle.getFriendAccount());
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // 先对消息进行持久化
            pointToPointMapper.insert(messageSingle);
            transactionManager.commit(status);
            MessageTypeEnum messageTypeEnum = MessageTypeEnum.getMessageType(messageSingle.getCommandType());
            ResponseMessageVo responseMessageVo = ResponseMessageVo.builder().messageContent(messageSingle.getMessage()).messageType(messageTypeEnum != null ? messageTypeEnum.getCode() : null).sender(messageSingle.getUserAccount()).receiver(messageSingle.getFriendAccount()).build();
            MessagePackage build = MessagePackage.completePackage(Constants.RECEIVED, ObjectUtil.serialize(responseMessageVo));
            if (ObjectUtil.isNotEmpty(channel)) {
                channelHandlerContext.writeAndFlush(MessagePackage.completePackage(ResponseEnum.RESPONSE_SUCCESS.getCode(), ResponseEnum.RESPONSE_SUCCESS.getMessage().getBytes(StandardCharsets.UTF_8)));
                channel.writeAndFlush(build);
            } else {
                // 好友不在线保存至redis列表中
                if (!redisTemplateUtils.lPushIfPresent(Constants.SERVER_USER_ACCOUNT + messageSingle.getFriendAccount(), build)) {
                    redisTemplateUtils.setList(Constants.SERVER_USER_ACCOUNT + messageSingle.getFriendAccount(), Collections.singletonList(build));
                }
            }
        } catch (Exception e) {
            log.error("好友互相发送消息处理失败! 消息体 = {}", JSON.toJSONString(messageSingle), e);
            transactionManager.rollback(status);
            channelHandlerContext.writeAndFlush(MessagePackage.completePackage(ResponseEnum.RESPONSE_FAIL.getCode(), ResponseEnum.RESPONSE_FAIL.getMessage().getBytes(StandardCharsets.UTF_8)));
        }
    }
}
