package com.xiaoma.im.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 17:17
 * @Email 1468835254@qq.com
 */
public interface HeatBeatHandler {

    void process(ChannelHandlerContext ctx) throws Exception ;
}
