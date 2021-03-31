package com.xiaoma.im.mq.producer;

import com.alibaba.fastjson.JSON;
import com.xiaoma.im.listener.service.impl.RocketMqTransactionTemplate;
import com.xiaoma.im.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.function.Supplier;

@Component
@Slf4j
public class TransactionProducer {

    @Resource
    private RocketMqTransactionTemplate mqTransactionTemplate;

    /**
     * Send Spring Message in Transaction
     *
     * @param destination 发送的目标处,格式如下: `topicName:tags`; tags没有的话就 "topicName"就可以
     * @param arg         ext arg 参数,在执行本地事务的时候可能会用到.目前未用到,传null即可
     * @param supplier    本地事务执行器
     * @return TransactionSendResult
     */
    public TransactionSendResult sendMessageInTransaction(String destination, MessageContent body, Object arg, Supplier<Boolean> supplier) {
        String uuid = UuidUtils.getUuid();
        Message<MessageContent> msg = MessageBuilder
                .withPayload(body)
                .setHeader(RocketMQHeaders.TRANSACTION_ID, uuid)
                .build();
        log.info("===>发送事务消息开始 destination=【{}】,body=【{}】,arg=【{}】,TRANSACTION_ID=【{}】", destination, JSON.toJSONString(msg), arg, uuid);
        TransactionSendResult transactionSendResult = mqTransactionTemplate.sendMessageInTransaction(destination, msg, arg, supplier);
        log.info("===>发送事务消息TRANSACTION_ID=【{}】结果 result =【{}】", uuid, transactionSendResult.getLocalTransactionState());
        return transactionSendResult;
    }

    /**
     * Send Spring Message in Transaction.但是不用arg参数
     *
     * @param destination destination 发送的目标处,格式如下: `topicName:tags`; tags没有的话就 "topicName"就可以
     * @param body        发送的消息
     * @param supplier    本地事务执行器
     * @return TransactionSendResult
     */
    public TransactionSendResult sendMessageInTransaction(String destination, MessageContent body, Supplier<Boolean> supplier) {
        return sendMessageInTransaction(destination, body, null, supplier);
    }


}
