package com.xiaoma.im.schedule;

import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.entity.UserStatus;
import com.xiaoma.im.utils.RedisTemplateUtils;
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

/**
 * @author admin
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@Configuration
@EnableScheduling
public class HeartBeatScheduleTask {

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    private static final String HEAT_BEAT = "HEAT_BEAT";

    @Scheduled(fixedRate = 10000)
    private void configureTasks() {
        Map<String, UserStatus> entries = redisTemplateUtils.getHashEntries(Constants.SERVER_REDIS_LIST);
        entries.keySet().forEach(key -> {
            UserStatus userStatus = entries.get(key);
            try {
                userStatus.getChannel().writeAndFlush(MessagePackage.completePackage(Constants.PING, HEAT_BEAT.getBytes(StandardCharsets.UTF_8)));
            } catch (Exception e) {
                userStatus.getChannel().close();
                redisTemplateUtils.delete(Constants.SERVER_REDIS_LIST, userStatus.getAccount());
            }
        });
    }
}