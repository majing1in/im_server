package com.xiaoma.im.utils;

import com.xiaoma.im.enums.ResponseEnum;

/**
 * @Author Xiaoma
 * @Date 2021/3/28 0028 22:01
 * @Email 1468835254@qq.com
 */
public class BaseResponseUtils {

    public static R<?> getNotFoundResponse() {
        return R.builder().code(ResponseEnum.RESPONSE_NOT_FIND.getCode()).message(ResponseEnum.RESPONSE_NOT_FIND.getMessage()).build();
    }

    public static R<?> getValidResponse() {
        return R.builder().code(ResponseEnum.RESPONSE_VALID.getCode()).message(ResponseEnum.RESPONSE_VALID.getMessage()).build();
    }

    public static R<?> getTimeoutResponse() {
        return R.builder().code(ResponseEnum.RESPONSE_TIMEOUT.getCode()).message(ResponseEnum.RESPONSE_TIMEOUT.getMessage()).build();
    }

    public static R<?> getUnknownResponse() {
        return R.builder().code(ResponseEnum.RESPONSE_TIMEOUT.getCode()).message(ResponseEnum.RESPONSE_TIMEOUT.getMessage()).build();
    }

    public static R<?> getSuccessResponse() {
        return R.builder().code(ResponseEnum.RESPONSE_SUCCESS.getCode()).message(ResponseEnum.RESPONSE_SUCCESS.getMessage()).build();
    }

    public static R<?> getSuccessResponse(String data) {
        return R.builder().code(ResponseEnum.RESPONSE_SUCCESS.getCode()).message(ResponseEnum.RESPONSE_SUCCESS.getMessage()).data(data).build();
    }

    public static R<?> getFailedResponse() {
        return R.builder().code(ResponseEnum.RESPONSE_FAIL.getCode()).message(ResponseEnum.RESPONSE_FAIL.getMessage()).build();
    }
}
