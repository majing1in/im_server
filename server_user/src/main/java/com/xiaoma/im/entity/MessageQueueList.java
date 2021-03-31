package com.xiaoma.im.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("message_queue_list")
public class MessageQueueList implements Serializable {
    private static final long serialVersionUID = 3099345143310168692L;

    private String messageId;

    private Integer isConsumption;

    private String messageContent;

    private Date createTime;

    private Date updateTime;

    public MessageQueueList() {
    }

    public MessageQueueList(String messageId, Integer isConsumption, String messageContent, Date createTime, Date updateTime) {
        this.messageId = messageId;
        this.isConsumption = isConsumption;
        this.messageContent = messageContent;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getIsConsumption() {
        return isConsumption;
    }

    public void setIsConsumption(Integer isConsumption) {
        this.isConsumption = isConsumption;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}