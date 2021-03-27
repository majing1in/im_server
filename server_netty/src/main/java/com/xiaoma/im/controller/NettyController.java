package com.xiaoma.im.controller;

import cn.hutool.core.util.ObjectUtil;
import com.xiaoma.im.enums.ResponseEnum;
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
    public R getChannelId(@RequestParam("userAccount") String userAccount) {
        NioSocketChannel nioSocketChannel = sessionSocketUtils.getNioSocketChannel(userAccount);
        if (ObjectUtil.isNotEmpty(nioSocketChannel)) {
            return R.builder().code(ResponseEnum.RESPONSE_SUCCESS.getCode()).message(ResponseEnum.RESPONSE_SUCCESS.getMessage()).data(nioSocketChannel.id().asLongText()).build();
        }
        return R.builder().code(ResponseEnum.RESPONSE_NOT_FIND.getCode()).message(ResponseEnum.RESPONSE_NOT_FIND.getMessage()).build();
    }

    @GetMapping("/deleteChannelId")
    public R deleteChannelId(@RequestParam("userAccount") String userAccount) {
        if (ObjectUtil.isNotEmpty(sessionSocketUtils.removeSessionMap(userAccount))) {
            return R.builder().code(ResponseEnum.RESPONSE_SUCCESS.getCode()).message(ResponseEnum.RESPONSE_SUCCESS.getMessage()).build();
        }
        return R.builder().code(ResponseEnum.RESPONSE_FAIL.getCode()).message(ResponseEnum.RESPONSE_FAIL.getMessage()).build();
    }
}
