package com.xiaoma.im.vo;

import com.xiaoma.im.entity.MessageGroupChat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author Xiaoma
 * @Date 2021/3/27 0027 11:33
 * @Email 1468835254@qq.com
 */
@Getter
@Setter
public class GroupChatVo extends MessageGroupChat implements Serializable {

    private static final long serialVersionUID = -5902655802798537531L;

    private String account;

    private String userNickName;

    public GroupChatVo() {
    }
}
