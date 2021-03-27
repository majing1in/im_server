package com.xiaoma.im.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author Xiaoma
 * @Date 2021/3/27 0027 11:51
 * @Email 1468835254@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGroupChatVo implements Serializable {

    private static final long serialVersionUID = 5094097503290059753L;

    private String groupName;
    private String groupPhoto;
    private String groupSign;
    private String ownerName;
    private String managerName;
    private Date createTime;
    private Date updateTime;
    private List<GroupUser> list;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupUser {
        private String userNickName;
        private String headPhoto;
        private Integer gender;
        private Date birthday;
        private String sign;
    }
}
