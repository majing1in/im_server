package com.xiaoma.im.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author Xiaoma
 * @Date 2021/2/16 0016 12:29
 * @Email 1468835254@qq.com
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessageVo implements Serializable {
    private static final long serialVersionUID = -2161809327808081932L;
    private Integer messageType;
    private String messageContent;
    private String sender;
    private String receiver;
}
