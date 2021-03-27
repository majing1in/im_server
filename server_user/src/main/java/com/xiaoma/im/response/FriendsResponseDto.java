package com.xiaoma.im.response;

import com.xiaoma.im.utils.DateUtils;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Xiaoma
 * @Date 2021/2/10 0010 15:41
 * @Email 1468835254@qq.com
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendsResponseDto implements Serializable {

    private static final long serialVersionUID = -5799960382876871630L;

    private String friendAccount;
    private String friendNickName;
    private Integer friendId;
    private String photo;
    private Date time;

    public static FriendsResponseDto completeFriendDto(String friendAccount, String friendNickName, Integer friendId, String photo) {
        Date date = DateUtils.localDateTimeConvertToDate();
        return new FriendsResponseDto(friendAccount, friendNickName, friendId, photo, date);
    }

}
