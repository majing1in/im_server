package com.xiaoma.im.constants;

/**
 * @Author Xiaoma
 * @Date 2021/2/7 0007 11:42
 * @Email 1468835254@qq.com
 */
public class Constants {

    /**
     * 服务端 注册码前缀 key
     */
    public static final String SERVER_REGISTER_TIMEOUT = "server_register_timeout_";

    /**
     * 服务端 单点登录 key
     */
    public static final String SERVER_SINGLE_LOGIN = "server_single_login_";
    /**
     * 服务端 用户在线 key
     */
    public static final String SERVER_ONLINE = "server_online_";

    public static final String SERVER_REDIS_LIST = "server_online_";

    /**
     * 服务端 用户权限
     */
    public static final String SERVER_ONLINE_AUTH = "server_auth_";

    /**
     * 服务端 保存用户未读消息列表
     */
    public static final String SERVER_USER_ACCOUNT = "server_user_messages_";

    public static final int SUCCESS = 1;
    public static final int FAILED = 2;
    /**
     * ping
     */
    public static final int PING = 3;

    /**
     * 发送消息
     */
    public static final int SEND = 5;

    /**
     * 接收
     */
    public static final int RECEIVED = 6;

    /**
     * 获取自己的信息
     */
    public static final int ME_INFO = 7;

    public static final int ME_INFO_UPDATE = 8;

    /**
     * 获取好友列表
     */
    public static final int FRIENDS_LIST = 9;

    /**
     * 获取好友列表
     */
    public static final int FRIEND_LIST_MESSAGE = 10;

    public static final int GROUP_LIST = 11;
    public static final int GROUP_LIST_MESSAGE = 12;
    public static final int GROUP_LIST_INFO = 13;

    /**
     * 获取好友列表
     */
    public static final int SERVER_CLOSE = 88;

    /**
     * 获取好友列表
     */
    public static final int SEND_RED_ENVELOPE = 100;

    public static final String OPTION_KEY = "userAccount";
    public static final String OPTION_VALUE = "channel";
}
