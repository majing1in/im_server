package com.xiaoma.im.vo;

import com.xiaoma.im.entity.UserInformation;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author Xiaoma
 * @Date 2021/2/7 0007 0:07
 * @Email 1468835254@qq.com
 */

@Getter
@Setter
public class UserInformationVo extends UserInformation {

    private static final long serialVersionUID = -6117917256685081514L;

    private String VerificationCode;

}
