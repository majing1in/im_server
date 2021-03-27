package com.xiaoma.im.utils;

import com.xiaoma.im.entity.MessagePackage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 17:32
 * @Email 1468835254@qq.com
 */
@Slf4j
public class NettyAttrUtil {

    private static final AttributeKey<String> ATTR_KEY_READER_TIME = AttributeKey.valueOf("readerTime");

    public static void updateReaderTime(Channel channel, Long time) {
        channel.attr(ATTR_KEY_READER_TIME).set(time.toString());
    }

    public static Long getReaderTime(Channel channel) {
        String value = getAttribute(channel);
        if (value != null) {
            return Long.valueOf(value);
        }
        return null;
    }

    private static String getAttribute(Channel channel) {
        Attribute<String> attr = channel.attr(NettyAttrUtil.ATTR_KEY_READER_TIME);
        return attr.get();
    }

    public static void isSendSuccess(ChannelHandlerContext channelHandlerContext, MessagePackage messagePackage) {
        ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(messagePackage).addListeners((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                future.channel().close();
            }
        });
        channelFuture.isSuccess();
    }
}
