package com.xiaoma.im.manager.friends;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.MessageGroupMapper;
import com.xiaoma.im.dao.MessageGroupUsersMapper;
import com.xiaoma.im.dao.UserInfoMapper;
import com.xiaoma.im.entity.MessageGroup;
import com.xiaoma.im.entity.MessageGroupUsers;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.entity.UserDetails;
import com.xiaoma.im.manager.HandlerBusiness;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Xiaoma
 * @Date 2021/3/27 0027 12:42
 * @Email 1468835254@qq.com
 */

@Service("group-list")
public class GroupListImpl implements HandlerBusiness {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private MessageGroupUsersMapper groupUsersMapper;

    @Resource
    private MessageGroupMapper groupMapper;

    @Override
    public void process(byte[] content, ChannelHandlerContext channelHandlerContext) {
        String account = new String(content, StandardCharsets.UTF_8);
        UserDetails userDetails = userInfoMapper.selectOne(new LambdaQueryWrapper<UserDetails>().eq(UserDetails::getUserAccount, account));
        List<MessageGroupUsers> groupUsers = groupUsersMapper.selectList(new LambdaQueryWrapper<MessageGroupUsers>().eq(MessageGroupUsers::getUserId, userDetails.getId()));
        if (ObjectUtil.isNull(groupUsers) || groupUsers.size() == 0) {
            return;
        }
        List<Integer> groupIds = new ArrayList<>();
        groupUsers.forEach(item -> groupIds.add(item.getGroupId()));
        List<MessageGroup> groups = new ArrayList<>();
        // 获取所有群详情
        List<MessageGroup> messageGroups = groupMapper.selectBatchIds(groupIds);
        // 组装返回参数
        messageGroups.forEach(item -> {
            MessageGroup group = new MessageGroup();
            group.setGroupId(item.getGroupId());
            group.setGroupName(item.getGroupName());
            group.setGroupSign(item.getGroupSign());
            group.setGroupPhoto(item.getGroupPhoto());
            groups.add(group);
        });
        channelHandlerContext.writeAndFlush(MessagePackage.completePackage(Constants.GROUP_LIST, ObjectUtil.serialize(groups)));
    }
}
