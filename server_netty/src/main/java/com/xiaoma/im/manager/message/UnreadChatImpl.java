package com.xiaoma.im.manager.message;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.enums.ResponseEnum;
import com.xiaoma.im.manager.HandlerBusiness;
import com.xiaoma.im.utils.RedisTemplateUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Xiaoma
 * @Date 2021/2/16 0016 11:56
 * @Email 1468835254@qq.com
 */
@SuppressWarnings("unchecked")
@Slf4j
@Service(value = "unread-handler")
public class UnreadChatImpl implements HandlerBusiness {

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Override
    public void process(byte[] content, ChannelHandlerContext channelHandlerContext) {
        String account = new String(content, StandardCharsets.UTF_8);
        List<MessagePackage> unreadList = (List<MessagePackage>) redisTemplateUtils.get(Constants.SERVER_USER_ACCOUNT + account);
        log.info("当前用户 {} 正在获取未读消息...", account);
        if (ObjectUtil.isEmpty(unreadList)) {
            channelHandlerContext.writeAndFlush(MessagePackage.completePackage(ResponseEnum.RESPONSE_NOT_FIND.getCode(), ResponseEnum.RESPONSE_NOT_FIND.getMessage().getBytes(StandardCharsets.UTF_8)));
        } else {
            unreadList.forEach(channelHandlerContext::writeAndFlush);
            redisTemplateUtils.delete(Constants.SERVER_USER_ACCOUNT + account);
        }
        log.info("当前用户 {} 获取未读消息完成! 获取消息 = {}", account, JSON.toJSONString(unreadList));
    }
}
