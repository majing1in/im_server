package com.xiaoma.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("group_information")
public class GroupInformation {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer groupId;

    private Integer groupOwnerId;

    private String groupName;

    private Integer groupManagerId;

    private String groupSign;

    private Date createTime;

    private Date updateTime;

    private String groupPhoto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupOwnerId() {
        return groupOwnerId;
    }

    public void setGroupOwnerId(Integer groupOwnerId) {
        this.groupOwnerId = groupOwnerId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Integer getGroupManagerId() {
        return groupManagerId;
    }

    public void setGroupManagerId(Integer groupManagerId) {
        this.groupManagerId = groupManagerId;
    }

    public String getGroupSign() {
        return groupSign;
    }

    public void setGroupSign(String groupSign) {
        this.groupSign = groupSign == null ? null : groupSign.trim();
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

    public String getGroupPhoto() {
        return groupPhoto;
    }

    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto == null ? null : groupPhoto.trim();
    }
}