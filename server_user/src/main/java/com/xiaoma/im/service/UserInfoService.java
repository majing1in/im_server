package com.xiaoma.im.service;

import com.xiaoma.im.entity.UserInformation;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 22:22
 * @Email 1468835254@qq.com
 */
public interface UserInfoService {

    UserInformation getUserInfoServiceById(Integer id);

    UserInformation getUserInfoServiceByAccount(String userAccount);

    boolean updateUserInfo(UserInformation userInformation);
}
