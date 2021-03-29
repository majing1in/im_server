package com.xiaoma.im.entity;

import java.io.Serializable;
import java.util.Date;

public class PointToPoint implements Serializable {
    private static final long serialVersionUID = 3099345143310168692L;
    private Integer id;

    private Integer senderId;

    private Integer receiverId;

    public PointToPoint() {
    }

    public PointToPoint(Integer id, Integer senderId, Integer receiverId, String messageContent, Integer messageType, Date createTime) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageContent = messageContent;
        this.messageType = messageType;
        this.createTime = createTime;
    }

    private String messageContent;

    private Integer messageType;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent == null ? null : messageContent.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}