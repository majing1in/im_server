package com.xiaoma.im.manager;

import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.utils.DateUtils;
import com.xiaoma.im.utils.NettyAttrUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author Xiaoma
 * @Date 2021/2/15 0015 21:25
 * @Email 1468835254@qq.com
 */
@Slf4j
@Service(value = "heat-beat")
public class HeatBeatImpl implements HandlerBusiness {

    @Override
    public void process(byte[] content, ChannelHandlerContext channelHandlerContext) {
        NettyAttrUtil.updateReaderTime(channelHandlerContext.channel(), DateUtils.getTimeStamp());
        MessagePackage build = MessagePackage.builder()
                .type(Constants.PING)
                .build();
        NettyAttrUtil.isSendSuccess(channelHandlerContext, build);
    }
}
