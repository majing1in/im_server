package com.xiaoma.im.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.FriendsInfoMapper;
import com.xiaoma.im.entity.FriendsInfo;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.entity.UserInfo;
import com.xiaoma.im.enums.CommandType;
import com.xiaoma.im.enums.ResponseEnum;
import com.xiaoma.im.response.FriendsResponseDto;
import com.xiaoma.im.service.UserInfoService;
import com.xiaoma.im.service.impl.FriendsServiceImpl;
import com.xiaoma.im.spi.FeignNettyServiceImpl;
import com.xiaoma.im.utils.R;
import com.xiaoma.im.utils.RedisTemplateUtils;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author Xiaoma
 * @Date 2021/2/10 0010 15:17
 * @Email 1468835254@qq.com
 */
@Slf4j
@RestController
@RequestMapping("/friends")
public class AboutFriendsController {

    @Resource
    private FriendsServiceImpl friendService;

    @Resource
    private FriendsInfoMapper friendsInfoMapper;

    @Resource
    private FeignNettyServiceImpl feignNettyServiceImpl;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private PlatformTransactionManager transactionManager;

    /**
     * 获取所有好友
     *
     * @param userId 自己的id
     * @return
     */
    @GetMapping("/list")
    public R getFriends(@RequestParam("userId") Integer userId) {
        if (ObjectUtil.isEmpty(userId)) {
            log.info("FriendController (getFriends) ==> 参数校验失败! time = {}", LocalDateTime.now());
            return R.builder().code(ResponseEnum.RESPONSE_VALID.getCode()).message(ResponseEnum.RESPONSE_VALID.getMessage()).build();
        }
        List<FriendsInfo> friendsList = friendService.getFriendsList(userId);
        log.info("FriendController (getFriends) ==> 获取好友列表成功! friends = {} time = {}", JSON.toJSONString(friendsList), LocalDateTime.now());
        return R.builder().code(ResponseEnum.RESPONSE_SUCCESS.getCode()).message(ResponseEnum.RESPONSE_SUCCESS.getMessage()).data(friendsList).build();
    }

    /**
     * 发送好友添加请求
     *
     * @param userAccount   接收方
     * @param friendAccount 发送发
     * @return
     */
    @GetMapping("/apply")
    public R applyToFriend(@RequestParam("userAccount") String userAccount, @RequestParam("friendAccount") String friendAccount, @RequestParam("nickname") String nickname) {
        log.info("好友添加请求 userAccount = {}, friendAccount = {}", userAccount, friendAccount);
        if (StringUtils.isBlank(userAccount) || StringUtils.isBlank(friendAccount)) {
            log.info("FriendController (applyToFriend) ==> 参数校验失败! time = {}", LocalDateTime.now());
            return R.builder().code(ResponseEnum.RESPONSE_VALID.getCode()).message(ResponseEnum.RESPONSE_VALID.getMessage()).build();
        }
        UserInfo userInfo = userInfoService.getUserInfoServiceByAccount(userAccount);
        UserInfo friendInfo = userInfoService.getUserInfoServiceByAccount(friendAccount);
        if (ObjectUtil.isEmpty(friendInfo)) {
            return R.builder().code(ResponseEnum.RESPONSE_NOT_FIND.getCode()).message(ResponseEnum.RESPONSE_NOT_FIND.getMessage()).build();
        }
        FriendsResponseDto friendsResponseDto = FriendsResponseDto.completeFriendDto(userInfo.getUserAccount(),nickname,userInfo.getId(),userInfo.getUserHeadPhoto());
        MessagePackage messagePackage = MessagePackage.completePackage(CommandType.COMMAND_APPLY.getCode(), ObjectUtil.serialize(friendsResponseDto));
        String channelId = feignNettyServiceImpl.getChannelId(friendAccount);
        if (StringUtils.isBlank(channelId)) {
            if (!redisTemplateUtils.lPushIfPresent(Constants.SERVER_USER_ACCOUNT + friendAccount, messagePackage)) {
                redisTemplateUtils.setList(Constants.SERVER_USER_ACCOUNT + friendAccount, Collections.singletonList(messagePackage));
            }
        } else {
            Map<String, Object> mapValue = redisTemplateUtils.getMapValue(channelId);
            NioSocketChannel channel = (NioSocketChannel) mapValue.get(Constants.OPTION_VALUE);
            channel.writeAndFlush(messagePackage);
        }
        log.info("FriendController (applyToFriend) ==> 好友添加操作完成! friendAccount = {} time = {}", friendAccount, LocalDateTime.now());
        return R.builder().code(ResponseEnum.RESPONSE_SUCCESS.getCode()).message(ResponseEnum.RESPONSE_SUCCESS.getMessage()).build();
    }

    @GetMapping("/agree")
    public R applyToAgree(@RequestParam("userAccount") String userAccount, @RequestParam("usernickname") String usernickname, @RequestParam("friendAccount") String friendAccount, @RequestParam("nickname") String nickname) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Integer userId = userInfoService.getUserInfoServiceByAccount(userAccount).getId();
            Integer friendId = userInfoService.getUserInfoServiceByAccount(friendAccount).getId();
            friendsInfoMapper.insert(FriendsInfo.createFriend(userId, friendId, nickname));
            friendsInfoMapper.insert(FriendsInfo.createFriend(friendId,userId,nickname));
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            return R.builder().code(ResponseEnum.RESPONSE_FAIL.getCode()).message(ResponseEnum.RESPONSE_FAIL.getMessage()).build();
        }
        return R.builder().code(ResponseEnum.RESPONSE_SUCCESS.getCode()).message(ResponseEnum.RESPONSE_SUCCESS.getMessage()).build();
    }
}
