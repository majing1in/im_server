package com.xiaoma.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.dao.FriendsRelationshipMapper;
import com.xiaoma.im.entity.FriendsRelationship;
import com.xiaoma.im.service.FriendsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Xiaoma
 * @Date 2021/2/10 0010 15:24
 * @Email 1468835254@qq.com
 */
@Service
public class FriendsServiceImpl implements FriendsService {

    @Resource
    private FriendsRelationshipMapper friendsRelationshipMapper;

    @Override
    public List<FriendsRelationship> getFriendsList(Integer userId) {
        List<FriendsRelationship> friendsRelationships = friendsRelationshipMapper.selectList(new LambdaQueryWrapper<FriendsRelationship>().eq(FriendsRelationship::getFriendUserId, userId));
        return friendsRelationships;
    }
}
