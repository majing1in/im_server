package com.xiaoma.im.mq.producer;

import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.utils.SpringBeanUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;

public class RocketMqProducer {

    private final RocketMQTemplate rocketMQTemplate = SpringBeanUtils.getBean(RocketMQTemplate.class);
    private String messageId;
    private MessagePackage body;
    private String destination;

    public RocketMqProducer(Builder builder) {
        this.messageId = builder.messageId;
        this.body = builder.body;
        this.destination = builder.destination;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String messageId;
        private MessagePackage body;
        private String destination;

        public RocketMqProducer create() {
            return new RocketMqProducer(this);
        }

        public Builder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder body(MessagePackage body) {
            this.body = body;
            return this;
        }

        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }
    }

    /**
     * 发送异步消息
     *
     * @param topic   消息Topic
     * @param message 消息实体
     */
    public void asyncSend(String topic, Message<?> message) {
        rocketMQTemplate.asyncSend(topic, message, getDefaultSendCallBack());
    }

    /**
     * 发送异步消息
     *
     * @param topic        消息Topic
     * @param message      消息实体
     * @param sendCallback 回调函数
     */
    public void asyncSend(String topic, Message<?> message, SendCallback sendCallback) {
        rocketMQTemplate.asyncSend(topic, message, sendCallback);
    }

    /**
     * 发送异步消息
     *
     * @param topic        消息Topic
     * @param message      消息实体
     * @param sendCallback 回调函数
     * @param timeout      超时时间
     */
    public void asyncSend(String topic, Message<?> message, SendCallback sendCallback, long timeout) {
        rocketMQTemplate.asyncSend(topic, message, sendCallback, timeout);
    }

    /**
     * 发送异步消息
     *
     * @param topic        消息Topic
     * @param message      消息实体
     * @param sendCallback 回调函数
     * @param timeout      超时时间
     * @param delayLevel   延迟消息的级别
     */
    public void asyncSend(String topic, Message<?> message, SendCallback sendCallback, long timeout, int delayLevel) {
        rocketMQTemplate.asyncSend(topic, message, sendCallback, timeout, delayLevel);
    }

    /**
     * 发送顺序消息
     *
     * @param message
     * @param topic
     * @param hashKey
     */
    public void syncSendOrderly(String topic, Message<?> message, String hashKey) {
        rocketMQTemplate.syncSendOrderly(topic, message, hashKey);
    }

    /**
     * 发送顺序消息
     *
     * @param message
     * @param topic
     * @param hashKey
     * @param timeout
     */
    public void syncSendOrderly(String topic, Message<?> message, String hashKey, long timeout) {
        rocketMQTemplate.syncSendOrderly(topic, message, hashKey, timeout);
    }


    /**
     * 默认CallBack函数
     *
     * @return
     */
    private SendCallback getDefaultSendCallBack() {
        return new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
            }

            @Override
            public void onException(Throwable throwable) {
            }
        };
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public MessagePackage getBody() {
        return body;
    }

    public void setBody(MessagePackage body) {
        this.body = body;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

}
