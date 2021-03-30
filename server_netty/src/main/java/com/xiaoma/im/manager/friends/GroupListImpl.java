package com.xiaoma.im.manager.friends;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.GroupInformationMapper;
import com.xiaoma.im.dao.GroupFriendsMapper;
import com.xiaoma.im.dao.UserInformationMapper;
import com.xiaoma.im.entity.GroupInformation;
import com.xiaoma.im.entity.GroupFriends;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.entity.UserInformation;
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
    private UserInformationMapper userInformationMapper;

    @Resource
    private GroupFriendsMapper groupUsersMapper;

    @Resource
    private GroupInformationMapper groupMapper;

    @Override
    public void process(byte[] content, ChannelHandlerContext channelHandlerContext) {
        String account = new String(content, StandardCharsets.UTF_8);
        UserInformation userInformation = userInformationMapper.selectOne(new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUserAccount, account));
        List<GroupFriends> groupUsers = groupUsersMapper.selectList(new LambdaQueryWrapper<GroupFriends>().eq(GroupFriends::getUserId, userInformation.getId()));
        if (ObjectUtil.isNull(groupUsers) || groupUsers.size() == 0) {
            return;
        }
        List<Integer> groupIds = new ArrayList<>();
        groupUsers.forEach(item -> groupIds.add(item.getGroupId()));
        List<GroupInformation> groups = new ArrayList<>();
        // 获取所有群详情
        List<GroupInformation> groupInformations = groupMapper.selectBatchIds(groupIds);
        // 组装返回参数
        groupInformations.forEach(item -> {
            GroupInformation group = new GroupInformation();
            group.setGroupId(item.getGroupId());
            group.setGroupName(item.getGroupName());
            group.setGroupSign(item.getGroupSign());
            group.setGroupPhoto(item.getGroupPhoto());
            groups.add(group);
        });
        channelHandlerContext.writeAndFlush(MessagePackage.completePackage(Constants.GROUP_LIST, ObjectUtil.serialize(groups)));
    }
}
