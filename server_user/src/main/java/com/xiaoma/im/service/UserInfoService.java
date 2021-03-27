package com.xiaoma.im.service;

import com.xiaoma.im.entity.UserInfo;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 22:22
 * @Email 1468835254@qq.com
 */
public interface UserInfoService {

    UserInfo getUserInfoServiceById(Integer id);

    UserInfo getUserInfoServiceByAccount(String userAccount);

    boolean updateUserInfo(UserInfo userInfo);
}
