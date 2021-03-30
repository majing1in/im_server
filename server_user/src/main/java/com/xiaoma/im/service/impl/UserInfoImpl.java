package com.xiaoma.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.dao.UserInformationMapper;
import com.xiaoma.im.entity.UserInformation;
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
    private UserInformationMapper userInformationMapper;

    @Override
    public UserInformation getUserInfoServiceById(Integer id) {
        return userInformationMapper.selectById(id);
    }

    @Override
    public UserInformation getUserInfoServiceByAccount(String userAccount) {
        return userInformationMapper.selectOne(new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUserAccount, userAccount));
    }

    @Override
    public boolean updateUserInfo(UserInformation userInformation) {
        int result = userInformationMapper.updateById(userInformation);
        return result > 0;
    }


}
