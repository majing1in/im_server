package com.xiaoma.im.controller;

import cn.hutool.core.util.ObjectUtil;
import com.xiaoma.im.utils.BaseResponseUtils;
import com.xiaoma.im.utils.R;
import com.xiaoma.im.utils.SessionSocketUtils;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 23:04
 * @Email 1468835254@qq.com
 */
@RestController
@RequestMapping("/netty")
public class NettyController {

    @Resource
    private SessionSocketUtils sessionSocketUtils;

    @GetMapping("/channelId")
    public R<?> getChannelId(@RequestParam("userAccount") String userAccount) {
        NioSocketChannel nioSocketChannel = sessionSocketUtils.getUserNioSocketChannelByAccount(userAccount);
        if (ObjectUtil.isNotEmpty(nioSocketChannel)) {
            return BaseResponseUtils.getSuccessResponse(nioSocketChannel.id().asLongText());
        }
        return BaseResponseUtils.getNotFoundResponse();
    }

    @GetMapping("/deleteChannelId")
    public R<?> deleteChannelId(@RequestParam("userAccount") String userAccount) {
        if (ObjectUtil.isNotEmpty(sessionSocketUtils.getUserNioSocketChannelByAccount(userAccount))) {
            return BaseResponseUtils.getSuccessResponse();
        }
        return BaseResponseUtils.getFailedResponse();
    }
}
