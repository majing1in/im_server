package com.xiaoma.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.dao.UserInfoMapper;
import com.xiaoma.im.entity.UserInfo;
import com.xiaoma.im.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 22:22
 * @Email 1468835254@qq.com
 */

@Service
public class UserInfoImpl implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo getUserInfoServiceById(Integer id) {
        return userInfoMapper.selectById(id);
    }

    @Override
    public UserInfo getUserInfoServiceByAccount(String userAccount) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUserAccount, userAccount));
    }

    @Override
    public boolean updateUserInfo(UserInfo userInfo) {
        int result = userInfoMapper.updateById(userInfo);
        return result > 0;
    }


}
