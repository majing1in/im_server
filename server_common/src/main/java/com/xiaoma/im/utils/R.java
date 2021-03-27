package com.xiaoma.im.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回数据
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    private Integer code;
    private String message;
    private T data;

    public R(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
