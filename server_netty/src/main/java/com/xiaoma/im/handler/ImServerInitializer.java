package com.xiaoma.im.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 0:24
 * @Email 1468835254@qq.com
 */
@Component
public class ImServerInitializer extends ChannelInitializer<SocketChannel> {

    @Resource
    private MyServerHandler myServerHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(30, 0, 0))
                .addLast("myDecoderHandler", new MyDecoderHandler())
                .addLast("myEncoderHandler", new MyEncoderHandler())
                .addLast("myServerHandler", myServerHandler);
    }
}
