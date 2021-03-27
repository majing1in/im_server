package com.xiaoma.im.schedule;

import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.utils.SessionSocketUtils;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
@Configuration
@EnableScheduling
public class HeartBeatScheduleTask {

    @Resource
    private SessionSocketUtils sessionSocketUtils;

    private static final String HEAT_BEAT = "HEAT_BEAT";

    @Scheduled(fixedRate = 7000)
    private void configureTasks() {
        Map<String, NioSocketChannel> sessionMap = sessionSocketUtils.getSessionMap();
        sessionMap.keySet().forEach(key -> {
            NioSocketChannel nioSocketChannel = sessionMap.get(key);
            try {
                nioSocketChannel.writeAndFlush(MessagePackage.completePackage(Constants.PING, HEAT_BEAT.getBytes(StandardCharsets.UTF_8)));
            } catch (Exception e) {
                sessionSocketUtils.removeSession(nioSocketChannel.id().asLongText());
                nioSocketChannel.close();
            }
        });
    }
}