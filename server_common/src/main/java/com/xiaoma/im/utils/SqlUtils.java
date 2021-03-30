package com.xiaoma.im.utils;

public class SqlUtils {

    public static String buildSqlForUserMoney(String account) {
        return "SELECT * from user_money WHERE uid = (SELECT id from user_info WHERE user_account ='" + account + "')";
    }
}
