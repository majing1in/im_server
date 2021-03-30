package com.xiaoma.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoma.im.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("friends_relationship")
public class FriendsRelationship {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer friendId;

    private Integer friendUserId;

    private String friendNickName;

    private Date createTime;

    private Date updateTime;

    public FriendsRelationship(Integer friendId, Integer friendUserId, String friendNickName, Date createTime, Date updateTime) {
        this.friendId = friendId;
        this.friendUserId = friendUserId;
        this.friendNickName = friendNickName;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public static FriendsRelationship createFriend(Integer friendId, Integer friendUserId, String friendNickName) {
        return new FriendsRelationship(friendId, friendUserId, friendNickName, DateUtils.localDateTimeConvertToDate(), DateUtils.localDateTimeConvertToDate());
    }

    public static FriendsRelationship updateFriend(Integer friendId, Integer friendUserId, String friendNickName) {
        return new FriendsRelationship(friendId, friendUserId, friendNickName, null, DateUtils.localDateTimeConvertToDate());
    }
}