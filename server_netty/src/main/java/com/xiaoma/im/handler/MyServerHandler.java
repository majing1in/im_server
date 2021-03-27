package com.xiaoma.im.handler;

import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.enums.BusinessHandlerEnum;
import com.xiaoma.im.manager.HandlerBusiness;
import com.xiaoma.im.utils.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 0:28
 * @Email 1468835254@qq.com
 */
@ChannelHandler.Sharable
@Component
@Slf4j
public class MyServerHandler extends SimpleChannelInboundHandler<MessagePackage> {


    @Resource
    private SessionSocketUtils sessionSocketUtils;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Resource
    private final Map<String, HandlerBusiness> map = new HashMap<>();



    /**
     * 业务处理
     *
     * @param channelHandlerContext 上下文
     * @param messagePackage        数据对象
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessagePackage messagePackage) throws Exception {
        int type = messagePackage.getType();
        byte[] content = messagePackage.getContent();
        String processor = BusinessHandlerEnum.getProcessor(type);
        if (StringUtils.isNotBlank(processor)) {
            HandlerBusiness handlerBusiness = map.get(processor);
            handlerBusiness.process(content, channelHandlerContext);
        }
    }

    /**
     * 心跳检测
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("定时检测客户端端是否存活");
                HeatBeatHandler heartBeatHandler = SpringBeanUtils.getBean(HeatBeatHandlerUtils.class);
                heartBeatHandler.process(ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 取消绑定
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 可能出现业务判断离线后再次触发 channelInactive
        Map<String, Object> mapValue = redisTemplateUtils.getMapValue(ctx.channel().id().asLongText());
        if (!mapValue.isEmpty()) {
            log.warn("[{}] trigger channelInactive offline!", mapValue.get("userAccount"));
            redisTemplateUtils.deleteData(new ArrayList<String>() {
                private static final long serialVersionUID = 6980127040041020089L;

                {
                    add(Constants.SERVER_ONLINE_AUTH + mapValue.get("userAccount"));
                }
            });
            sessionSocketUtils.removeSession(ctx.channel().id().asLongText());
            ctx.channel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String id = ctx.channel().id().asLongText();
        sessionSocketUtils.removeSession(id);
        ctx.channel().close();
        log.error(" 服务端发送错误！关闭连接！", cause);
    }

}
