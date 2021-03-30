package com.xiaoma.im.service;

import com.xiaoma.im.entity.FriendsRelationship;

import java.util.List;

/**
 * @Author Xiaoma
 * @Date 2021/2/10 0010 15:23
 * @Email 1468835254@qq.com
 */
public interface FriendsService {

    List<FriendsRelationship> getFriendsList(Integer userId);
}
