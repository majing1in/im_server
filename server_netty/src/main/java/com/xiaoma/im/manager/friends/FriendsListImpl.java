package com.xiaoma.im.manager.friends;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.FriendsRelationshipMapper;
import com.xiaoma.im.dao.UserInformationMapper;
import com.xiaoma.im.entity.FriendsRelationship;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.entity.UserInformation;
import com.xiaoma.im.manager.HandlerBusiness;
import com.xiaoma.im.vo.FriendsListVo;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Xiaoma
 * @Date 2021/2/15 0015 21:28
 * @Email 1468835254@qq.com
 */
@Slf4j
@Service(value = "friend-list")
public class FriendsListImpl implements HandlerBusiness {

    @Resource
    private UserInformationMapper userInformationMapper;

    @Resource
    private FriendsRelationshipMapper friendsRelationshipMapper;

    @Override
    public void process(byte[] content, ChannelHandlerContext channelHandlerContext) {
        String account = new String(content, StandardCharsets.UTF_8);
        UserInformation userInformation = userInformationMapper.selectOne(new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUserAccount, account));
        log.info("当前用户 {} 正在获取好友列表...", account);
        List<FriendsRelationship> friendsRelationshipList = friendsRelationshipMapper.selectList(new LambdaQueryWrapper<FriendsRelationship>().eq(FriendsRelationship::getFriendUserId, userInformation.getId()));
        log.info("当前用户 {} 获取好友信息 = {}", account, JSON.toJSONString(friendsRelationshipList));
        if (!ObjectUtil.isEmpty(friendsRelationshipList) && friendsRelationshipList.size() != 0) {
            List<Integer> ids = new ArrayList<>();
            friendsRelationshipList.forEach(friendsRelationship -> { ids.add(friendsRelationship.getFriendId()); });
            List<UserInformation> details = userInformationMapper.selectBatchIds(ids);
            List<FriendsListVo> result = new ArrayList<>();
            AtomicInteger index = new AtomicInteger();
            friendsRelationshipList.forEach(friendsRelationship -> {
                FriendsListVo vo = new FriendsListVo();
                vo.setId(details.get(index.get()).getId());
                vo.setNickName(friendsRelationship.getFriendNickName());
                vo.setUserAccount(details.get(index.get()).getUserAccount());
                vo.setUserHeadPhoto(details.get(index.get()).getUserHeadPhoto());
                vo.setUserSign(details.get(index.get()).getUserSign());
                vo.setBuildTime(friendsRelationship.getCreateTime());
                vo.setCreateTime(details.get(index.get()).getCreateTime());
                result.add(vo);
                index.getAndIncrement();
            });
            channelHandlerContext.writeAndFlush(MessagePackage.completePackage(Constants.FRIENDS_LIST, ObjectUtil.serialize(result)));
        }
        log.info("当前用户 {} 获取好友列表结束...", account);
    }
}
