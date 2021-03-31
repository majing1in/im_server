package com.xiaoma.im.listener.service;

import com.xiaoma.im.mq.producer.MessageContent;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.springframework.messaging.Message;

import java.util.function.Supplier;

public interface MqTransactionTemplate {

    /**
     * 发送事务消息
     * @param destination
     * @param message
     * @param arg
     * @param supplier 本地事务执行逻辑
     * @return
     */
    TransactionSendResult sendMessageInTransaction(String destination, Message<MessageContent> message, Object arg, Supplier<Boolean> supplier);

}
