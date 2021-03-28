package com.xiaoma.im.utils;

import cn.hutool.core.util.ObjectUtil;
import com.xiaoma.im.handler.HeatBeatHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 17:17
 * @Email 1468835254@qq.com
 */
@Slf4j
@Component
public class HeatBeatHandlerUtils implements HeatBeatHandler {

    private static final Long HEARTBEAT_TIME = 1000 * 10L;

    @Resource
    private SessionSocketUtils sessionSocketUtils;

    @Override
    public void process(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String id = channel.id().asLongText();
        Long lastReadTime = NettyAttrUtil.getReaderTime(ctx.channel());
        long now = DateUtils.getTimeStamp();
        SessionSocketUtils.UserStatus userStatus = sessionSocketUtils.getUserStatusById(ctx.channel().id().asLongText());
        if (lastReadTime != null && now - lastReadTime > HEARTBEAT_TIME) {
            if (ObjectUtil.isNotNull(userStatus)) {
                sessionSocketUtils.removeSessionByAccount(userStatus.getAccount());
                log.warn("客户端[{}]心跳超时[{}]ms，需要关闭连接!", userStatus.getAccount(), now - lastReadTime);
            }
            ctx.channel().close();
        } else {
            log.info("客户端[{}] 心跳检测! time = {}", userStatus.getAccount(), now - lastReadTime);
        }
    }
}
