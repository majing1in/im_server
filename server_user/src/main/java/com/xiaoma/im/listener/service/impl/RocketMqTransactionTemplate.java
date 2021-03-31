package com.xiaoma.im.listener.service.impl;

import com.xiaoma.im.listener.TransactionContainer;
import com.xiaoma.im.listener.service.MqTransactionTemplate;
import com.xiaoma.im.mq.producer.MessageContent;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.function.Supplier;

@Component
public class RocketMqTransactionTemplate implements MqTransactionTemplate {

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource



    private TransactionContainer transactionContainer;

    @Override
    public TransactionSendResult sendMessageInTransaction(String destination, Message<MessageContent> message, Object arg, Supplier<Boolean> supplier) {
        String mqTxId = (String) message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        transactionContainer.put(mqTxId, supplier);
        return rocketMQTemplate.sendMessageInTransaction(destination, message, arg);
    }

}
