package com.xiaoma.im.entity;

import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author admin
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatus {
    private Date loginTime;
    private NioSocketChannel channel;
    private String account;
    private Integer status;
}