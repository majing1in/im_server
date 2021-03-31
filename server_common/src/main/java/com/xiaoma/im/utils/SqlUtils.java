package com.xiaoma.im.utils;

import java.util.Date;

public class SqlUtils {

    public static String buildSqlForUserMoney(String account) {
        return "SELECT * from user_money WHERE uid = (SELECT id from user_info WHERE user_account ='" + account + "')";
    }

    public static String buildSqlForInsertMessage(String messageId, Integer isConsumption, String messageContent, Date createTime, Date updateTime) {
        return "INSERT INTO `message_queue_list`(`message_id`,`is_consumption`,`message_content`,`create_time`,`update_time`) VALUES (" + messageId + ", "+isConsumption+","+messageContent+","+createTime+","+updateTime+")";
    }

    public static String buildSqlForUpdateMessage(String messageId) {
        return "SELECT COUNT(1) FROM `message_queue_list` WHERE `message_id` = " + messageId;
    }
}
