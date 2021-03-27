package com.xiaoma.im.manager.user;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.UserInfoMapper;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.entity.UserDetails;
import com.xiaoma.im.manager.HandlerBusiness;
import com.xiaoma.im.utils.NettyAttrUtil;
import com.xiaoma.im.utils.SessionSocketUtils;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @Author Xiaoma
 * @Date 2021/2/15 0015 21:26
 * @Email 1468835254@qq.com
 */
@Slf4j
@Service(value = "user-info")
public class PersonalDetailsImpl implements HandlerBusiness {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private SessionSocketUtils sessionSocketUtils;

    @Override
    public void process(byte[] content, ChannelHandlerContext channelHandlerContext) {
        String account = new String(content, StandardCharsets.UTF_8);
        sessionSocketUtils.saveSession(account, (NioSocketChannel) channelHandlerContext.channel());
        UserDetails userDetails = userInfoMapper.selectOne(new LambdaQueryWrapper<UserDetails>().eq(UserDetails::getUserAccount, account));
        MessagePackage build = MessagePackage.completePackage(Constants.ME_INFO, ObjectUtil.serialize(userDetails));
        NettyAttrUtil.isSendSuccess(channelHandlerContext, build);
    }
}
