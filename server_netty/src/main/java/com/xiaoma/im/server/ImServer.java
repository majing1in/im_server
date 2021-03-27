package com.xiaoma.im.server;

import com.xiaoma.im.handler.ImServerInitializer;
import com.xiaoma.im.utils.SessionSocketUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 0:02
 * @Email 1468835254@qq.com
 */

@Slf4j
@Order(value = 1)
@Component
public class ImServer implements CommandLineRunner {

    @Resource
    private ImServerInitializer imServerInitializer;

    @Resource
    private SessionSocketUtils sessionSocketUtils;

    private static final Integer PORT = 8003;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public ImServer() {
    }

    public void initImServer() {
        log.info("netty server init start！");
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(PORT))
                    .childHandler(imServerInitializer)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind().sync();
            if (future.isSuccess()) {
                log.info("netty server init end！");
            }
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.info("netty server init fail！", e);
        }
    }

    @PreDestroy
    public void destroyInServer() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        sessionSocketUtils.clearSession();
    }

    @Async
    @Override
    public void run(String... args) throws Exception {
        initImServer();
    }
}
