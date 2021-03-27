package com.xiaoma.im.entity;

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
public class FriendsInfo {
    private Integer id;

    private Integer friendId;

    private Integer friendUserId;

    private String friendNickName;

    private Date createTime;

    private Date updateTime;

    public FriendsInfo( Integer friendId, Integer friendUserId, String friendNickName, Date createTime, Date updateTime) {
        this.friendId = friendId;
        this.friendUserId = friendUserId;
        this.friendNickName = friendNickName;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public static FriendsInfo createFriend(Integer friendId, Integer friendUserId, String friendNickName) {
        return new FriendsInfo(friendId, friendUserId, friendNickName, DateUtils.localDateTimeConvertToDate(), DateUtils.localDateTimeConvertToDate());
    }

    public static FriendsInfo updateFriend(Integer friendId, Integer friendUserId, String friendNickName) {
        return new FriendsInfo(friendId, friendUserId, friendNickName, null, DateUtils.localDateTimeConvertToDate());
    }
}