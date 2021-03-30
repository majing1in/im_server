package com.xiaoma.im.manager.friends;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.PrivateChatListMapper;
import com.xiaoma.im.dao.UserInformationMapper;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.entity.PointToPoint;
import com.xiaoma.im.entity.UserInformation;
import com.xiaoma.im.manager.HandlerBusiness;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author Xiaoma
 * @Date 2021/2/16 0016 15:09
 * @Email 1468835254@qq.com
 */
@Slf4j
@Service(value = "friend-message")
public class RecordChatImpl implements HandlerBusiness {

    @Resource
    private PrivateChatListMapper privateChatListMapper;

    @Resource
    private UserInformationMapper userInformationMapper;

    @Override
    public void process(byte[] content, ChannelHandlerContext channelHandlerContext) {
        String list = new String(content, StandardCharsets.UTF_8);
        String[] split = list.split(",");
        Integer id = userInformationMapper.selectOne(new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUserAccount, split[0])).getId();
        List<PointToPoint> singleList = privateChatListMapper.selectList(new LambdaQueryWrapper<PointToPoint>()
                .eq(PointToPoint::getReceiverId, id)
                .and(i -> i.eq(PointToPoint::getSenderId, Integer.valueOf(split[1])))
                .or()
                .eq(PointToPoint::getReceiverId, Integer.valueOf(split[1]))
                .and(i -> i.eq(PointToPoint::getSenderId, id)));
        channelHandlerContext.writeAndFlush(MessagePackage.completePackage(Constants.FRIEND_LIST_MESSAGE, ObjectUtil.serialize(singleList)));
    }
}
