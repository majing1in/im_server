package com.xiaoma.im.handler;

import cn.hutool.core.util.ObjectUtil;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.entity.MessagePackage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @Author Xiaoma
* @Date  2021/2/8 0008 0:27
* @Email 1468835254@qq.com
*/
@Slf4j
public class MyDecoderHandler extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        MessagePackage.MessagePackageBuilder packageBuilder = MessagePackage.builder();
        int type = byteBuf.readInt();
        log.info("MyDecoderHandler TYPE = {}", type);
        if (ObjectUtil.equals(type, Constants.PING)) {
            packageBuilder.type(type);
        } else {
            int length = byteBuf.readInt();
            byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            packageBuilder.length(length).type(type).content(bytes);
        }
        list.add(packageBuilder.build());
    }
}
