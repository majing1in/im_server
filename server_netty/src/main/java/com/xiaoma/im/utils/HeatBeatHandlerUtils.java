package com.xiaoma.im.utils;

import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.handler.HeatBeatHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

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
    private RedisTemplateUtils redisTemplateUtils;

    @Resource
    private SessionSocketUtils sessionSocketUtils;

    @Override
    public void process(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String id = channel.id().asLongText();
        Long lastReadTime = NettyAttrUtil.getReaderTime(ctx.channel());
        long now = DateUtils.getTimeStamp();
        Map<String, Object> map = redisTemplateUtils.getMapValue(Constants.SERVER_ONLINE + id);
        if (lastReadTime != null && now - lastReadTime > HEARTBEAT_TIME) {
            if (!map.isEmpty()) {
                sessionSocketUtils.removeSession(id);
                log.warn("客户端[{}]心跳超时[{}]ms，需要关闭连接!", map.get("userAccount"), now - lastReadTime);
            }
            ctx.channel().close();
        } else {
            log.info("客户端[{}] 心跳检测! time = {}", map.get("userAccount"), now - lastReadTime);
        }
    }
}
