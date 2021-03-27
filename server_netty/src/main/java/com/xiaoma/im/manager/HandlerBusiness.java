package com.xiaoma.im.manager;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Author Xiaoma
 * @Date 2021/2/15 0015 21:24
 * @Email 1468835254@qq.com
 */
public interface HandlerBusiness {

    void process(byte[] content, ChannelHandlerContext channelHandlerContext);
}
