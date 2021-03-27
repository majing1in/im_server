package com.xiaoma.im.vo;

import com.xiaoma.im.entity.UserDetails;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Xiaoma
 * @Date 2021/2/15 0015 22:01
 * @Email 1468835254@qq.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FriendsListVo extends UserDetails implements Serializable {

    private static final long serialVersionUID = -1333651779662337960L;

    private String nickName;

    private Date buildTime;

}
