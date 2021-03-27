package com.xiaoma.im.manager.friends;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.system.UserInfo;
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
import com.xiaoma.im.vo.ResponseGroupChatVo;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Xiaoma
 * @Date 2021/3/27 0027 12:59
 * @Email 1468835254@qq.com
 */
@Slf4j
@Service("group-info")
public class GroupInfoImpl implements HandlerBusiness {

    @Resource
    private MessageGroupUsersMapper groupUsersMapper;

    @Resource
    private MessageGroupMapper groupMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public void process(byte[] content, ChannelHandlerContext channelHandlerContext) {
        Integer groupId = Integer.parseInt(new String(content, StandardCharsets.UTF_8));
        MessageGroup group = groupMapper.selectOne(new LambdaQueryWrapper<MessageGroup>().eq(MessageGroup::getGroupId, groupId));
        List<MessageGroupUsers> groupUsers = groupUsersMapper.selectList(new LambdaQueryWrapper<MessageGroupUsers>().eq(MessageGroupUsers::getGroupId, groupId));
        List<Integer> userIds = new ArrayList<>();
        groupUsers.forEach(item -> userIds.add(item.getUserId()));
        // 获取该群所有用户
        List<UserDetails> userDetails = userInfoMapper.selectBatchIds(userIds);
        ResponseGroupChatVo vo = new ResponseGroupChatVo();
        vo.setGroupName(group.getGroupName());
        vo.setGroupSign(group.getGroupSign());
        vo.setCreateTime(group.getCreateTime());
        vo.setUpdateTime(group.getUpdateTime());
        vo.setGroupPhoto(group.getGroupPhoto());
        List<ResponseGroupChatVo.GroupUser> users = new ArrayList<>();
        userDetails.forEach(item -> {
            // 设置群主
            if (ObjectUtil.equals(item.getId(), group.getGroupOwnerId())) {
                vo.setOwnerName(item.getUserNickName());
            }
            // 设置管理员
            if (ObjectUtil.equals(item.getId(), group.getGroupManagerId())) {
                vo.setOwnerName(item.getUserNickName());
            }
            ResponseGroupChatVo.GroupUser groupUser = new ResponseGroupChatVo.GroupUser();
            groupUser.setGender(item.getUserGender());
            groupUser.setHeadPhoto(item.getUserHeadPhoto());
            groupUser.setBirthday(item.getUserBirthday());
            groupUser.setSign(item.getUserSign());
            groupUser.setUserNickName(item.getUserNickName());
            users.add(groupUser);
        });
        vo.setList(users);
        channelHandlerContext.writeAndFlush(MessagePackage.completePackage(Constants.GROUP_LIST_INFO, ObjectUtil.serialize(vo)));
    }
}
