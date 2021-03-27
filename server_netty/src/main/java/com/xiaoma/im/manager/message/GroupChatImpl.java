package com.xiaoma.im.manager.message;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.MessageGroupChatMapper;
import com.xiaoma.im.dao.MessageGroupUsersMapper;
import com.xiaoma.im.dao.UserInfoMapper;
import com.xiaoma.im.entity.MessageGroupUsers;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.entity.UserDetails;
import com.xiaoma.im.manager.HandlerBusiness;
import com.xiaoma.im.utils.RedisTemplateUtils;
import com.xiaoma.im.utils.SessionSocketUtils;
import com.xiaoma.im.vo.GroupChatVo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Xiaoma
 * @Date 2021/3/27 0027 11:28
 * @Email 1468835254@qq.com
 */

@Service("group-send")
public class GroupChatImpl implements HandlerBusiness {

    @Resource
    private MessageGroupChatMapper groupChatMapper;

    @Resource
    private MessageGroupUsersMapper usersMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private SessionSocketUtils sessionSocketUtils;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Resource
    private PlatformTransactionManager transactionManager;

    @Override
    public void process(byte[] content, ChannelHandlerContext channelHandlerContext) {
        GroupChatVo groupChatVo = ObjectUtil.deserialize(content);
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            groupChatMapper.insert(groupChatVo);
            List<MessageGroupUsers> groupUsers = usersMapper.selectList(new LambdaQueryWrapper<MessageGroupUsers>().eq(MessageGroupUsers::getGroupId, groupChatVo.getGroupId()));
            List<Integer> userIds = new ArrayList<>();
            groupUsers.forEach(item -> {
                userIds.add(item.getUserId());
            });
            // 封装消息包
            GroupChatVo chatVo = new GroupChatVo();
            chatVo.setUserNickName(groupChatVo.getUserNickName());
            chatVo.setMessageType(groupChatVo.getMessageType());
            chatVo.setGroupId(groupChatVo.getGroupId());
            chatVo.setMessageContent(groupChatVo.getMessageContent());
            chatVo.setCreateTime(groupChatVo.getCreateTime());
            MessagePackage messagePackage = MessagePackage.completePackage(Constants.RECEIVED, ObjectUtil.serialize(chatVo));
            // 获取在当前group中的用户
            List<UserDetails> userDetails = userInfoMapper.selectBatchIds(userIds);
            // 获取用户是否在线关联关系
            for (int i = userDetails.size() - 1; i >= 0; i--) {
                NioSocketChannel socketChannel = sessionSocketUtils.getNioSocketChannel(userDetails.get(i).getUserAccount());
                if (ObjectUtil.isNotNull(socketChannel)) {
                    socketChannel.writeAndFlush(messagePackage);
                    userDetails.remove(i);
                }
            }
            // 不在线直接加入Redis队列中
            if (userDetails.size() > 0) {
                userDetails.forEach(item -> {
                    if (!redisTemplateUtils.lPushIfPresent(Constants.SERVER_USER_ACCOUNT + item.getUserAccount(), messagePackage)) {
                        redisTemplateUtils.setList(Constants.SERVER_USER_ACCOUNT + item.getUserAccount(), Collections.singletonList(messagePackage));
                    }
                });
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }
    }
}
