package com.xiaoma.im.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.xiaoma.im.mq.producer.MessageContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Author Xiaoma
 * @Date 2021/3/30 0030 21:41
 * @Email 1468835254@qq.com
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.topic}", consumerGroup = "${mq.group}", consumeThreadMax = 2)
public class Consumption implements RocketMQListener<MessageContent> {

    @Override
    public void onMessage(MessageContent message) {
        log.info("message = {}", JSON.toJSONString(message));
    }
}
