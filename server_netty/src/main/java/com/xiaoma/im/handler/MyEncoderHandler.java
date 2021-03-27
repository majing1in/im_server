package com.xiaoma.im.handler;

import cn.hutool.core.util.ObjectUtil;
import com.xiaoma.im.entity.MessagePackage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 0:28
 * @Email 1468835254@qq.com
 */
@Slf4j
public class MyEncoderHandler extends MessageToByteEncoder<MessagePackage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessagePackage messagePackage, ByteBuf byteBuf) throws Exception {
        if (ObjectUtil.isNotEmpty(messagePackage.getType()))
            byteBuf.writeInt(messagePackage.getType());
        if (ObjectUtil.isNotEmpty(messagePackage.getLength()))
            byteBuf.writeInt(messagePackage.getLength());
        if (ObjectUtil.isNotEmpty(messagePackage.getContent()))
            byteBuf.writeBytes(messagePackage.getContent());
    }
}
