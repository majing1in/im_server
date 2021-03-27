package com.xiaoma.im.enums;

import com.xiaoma.im.constants.Constants;

/**
 * @Author Xiaoma
 * @Date 2021/2/15 0015 21:30
 * @Email 1468835254@qq.com
 */
public enum BusinessHandlerEnum {
    BUSINESS_FRIEND_LIST(Constants.FRIENDS_LIST, "friend-list"),
    BUSINESS_HEAT_BEAT(Constants.PING, "heat-beat"),
    BUSINESS_USER_INFO(Constants.ME_INFO, "user-info"),
    BUSINESS_USER_INFO_UPDATE(Constants.ME_INFO_UPDATE, "user-info-update"),
    BUSINESS_USER_SEND(Constants.SEND, "send-single"),
    BUSINESS_USER_RECEIVED(Constants.RECEIVED, "unread-handler"),
    BUSINESS_FRIEND_MESSAGE(Constants.FRIEND_LIST_MESSAGE, "friend-message"),
    // group
    BUSINESS_GROUP_LIST(Constants.GROUP_LIST, "group-list"),
    BUSINESS_GROUP_MESSAGE(Constants.GROUP_LIST_MESSAGE, "group-send"),
    BUSINESS_GROUP_INFO(Constants.GROUP_LIST_INFO, "group-info"),
    ;

    BusinessHandlerEnum(Integer code, String processor) {
        this.code = code;
        this.processor = processor;
    }

    private Integer code;
    private String processor;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public static String getProcessor(int code) {
        BusinessHandlerEnum[] values = BusinessHandlerEnum.values();
        for (BusinessHandlerEnum value : values) {
            if (value.code == code) {
                return value.processor;
            }
        }
        return null;
    }
}
