package com.xiaoma.im.enums;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 22:42
 * @Email 1468835254@qq.com
 */
public enum CommandType {
    COMMAND_LOGIN(31,"登录"),
    COMMAND_REGISTER(32,"注册"),
    COMMAND_UNLINE(33,"注销"),
    COMMAND_HEATBEAT(34,"心跳包"),
    COMMAND_SEND(35,"发送消息"),
    COMMAND_RECEIVE(36,"接收消息"),
    COMMAND_APPLY(37,"好友申请"),
    COMMAND_MESSAGE(38,"未读消息"),
    COMMAND_PACKAGE(39,"红包来了")
    ;

    private Integer code;
    private String message;

    CommandType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
