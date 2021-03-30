package com.xiaoma.im.mq.producer;

import com.xiaoma.im.entity.MessagePackage;
import lombok.Builder;
import org.springframework.messaging.support.GenericMessage;

/**
 * @Author Xiaoma
 * @Date 2021/3/30 0030 21:56
 * @Email 1468835254@qq.com
 */
@Builder
public class MessageContent {

    public static GenericMessage<MessageContent> getMessageContent(String messageId, MessagePackage body) {
        MessageContent content = new MessageContent(messageId, body);
        return new GenericMessage<MessageContent>(content);
    }

    private String messageId;
    private MessagePackage body;

    public MessageContent() {
    }

    public MessageContent(String messageId, MessagePackage body) {
        this.messageId = messageId;
        this.body = body;
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
}
