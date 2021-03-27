package com.xiaoma.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.dao.FriendsInfoMapper;
import com.xiaoma.im.entity.FriendsInfo;
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
    private FriendsInfoMapper friendsInfoMapper;

    @Override
    public List<FriendsInfo> getFriendsList(Integer userId) {
        List<FriendsInfo> friendsInfos = friendsInfoMapper.selectList(new LambdaQueryWrapper<FriendsInfo>().eq(FriendsInfo::getFriendUserId, userId));
        return friendsInfos;
    }
}
