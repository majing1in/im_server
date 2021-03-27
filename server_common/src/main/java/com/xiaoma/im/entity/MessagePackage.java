package com.xiaoma.im.entity;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 12:55
 * @Email 1468835254@qq.com
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePackage implements Serializable {

    private static final long serialVersionUID = 5040602611581921856L;
    /**
     * 消息长度
     */
    private int length;
    /**
     * 消息类型
     */
    private int type;
    /**
     * 消息体
     */
    private byte[] content;

    public static MessagePackage completePackage(int type, byte[] content) {
        int length = ObjectUtil.serialize(content).length;
        return new MessagePackage(length, type, content);
    }
}
