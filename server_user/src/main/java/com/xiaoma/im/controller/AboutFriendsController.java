package com.xiaoma.im.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.FriendsInfoMapper;
import com.xiaoma.im.entity.FriendsInfo;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.entity.UserInfo;
import com.xiaoma.im.entity.UserStatus;
import com.xiaoma.im.enums.CommandType;
import com.xiaoma.im.enums.ResponseEnum;
import com.xiaoma.im.response.FriendsResponseDto;
import com.xiaoma.im.service.UserInfoService;
import com.xiaoma.im.service.impl.FriendsServiceImpl;
import com.xiaoma.im.spi.FeignNettyServiceImpl;
import com.xiaoma.im.utils.BaseResponseUtils;
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
@SuppressWarnings("unchecked")
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
    public R<?> getFriends(@RequestParam("userId") Integer userId) {
        if (ObjectUtil.isEmpty(userId)) {
            log.info("FriendController (getFriends) ==> 参数校验失败! time = {}", LocalDateTime.now());
            return BaseResponseUtils.getFailedResponse();
        }
        List<FriendsInfo> friendsList = friendService.getFriendsList(userId);
        log.info("FriendController (getFriends) ==> 获取好友列表成功! friends = {} time = {}", JSON.toJSONString(friendsList), LocalDateTime.now());
        return BaseResponseUtils.getSuccessResponse();
    }

    /**
     * 发送好友添加请求
     *
     * @param userAccount   接收方
     * @param friendAccount 发送发
     * @return
     */
    @GetMapping("/apply")
    public R<?> applyToFriend(@RequestParam("userAccount") String userAccount, @RequestParam("friendAccount") String friendAccount, @RequestParam("nickname") String nickname) {
        log.info("好友添加请求 userAccount = {}, friendAccount = {}", userAccount, friendAccount);
        if (StringUtils.isBlank(userAccount) || StringUtils.isBlank(friendAccount)) {
            log.info("FriendController (applyToFriend) ==> 参数校验失败! time = {}", LocalDateTime.now());
            return BaseResponseUtils.getValidResponse();
        }
        UserInfo userInfo = userInfoService.getUserInfoServiceByAccount(userAccount);
        UserInfo friendInfo = userInfoService.getUserInfoServiceByAccount(friendAccount);
        if (ObjectUtil.isEmpty(friendInfo)) {
            return BaseResponseUtils.getNotFoundResponse();
        }
        FriendsResponseDto friendsResponseDto = FriendsResponseDto.completeFriendDto(userInfo.getUserAccount(), nickname, userInfo.getId(), userInfo.getUserHeadPhoto());
        MessagePackage messagePackage = MessagePackage.completePackage(CommandType.COMMAND_APPLY.getCode(), ObjectUtil.serialize(friendsResponseDto));
        String channelId = feignNettyServiceImpl.getChannelId(friendAccount);
        if (StringUtils.isBlank(channelId)) {
            if (redisTemplateUtils.rightPush(Constants.SERVER_USER_ACCOUNT + friendAccount, messagePackage) > 0) {
                return BaseResponseUtils.getSuccessResponse();
            }
        } else {
            UserStatus userStatus = (UserStatus) redisTemplateUtils.get(Constants.SERVER_USER_ACCOUNT + friendAccount);
            NioSocketChannel channel = userStatus.getChannel();
            channel.writeAndFlush(messagePackage);
        }
        log.info("FriendController (applyToFriend) ==> 好友添加操作完成! friendAccount = {} time = {}", friendAccount, LocalDateTime.now());
        return BaseResponseUtils.getSuccessResponse();
    }

    /**
     * 同意好友请求
     *
     * @param userAccount
     * @param usernickname
     * @param friendAccount
     * @param nickname
     * @return
     */
    @GetMapping("/agree")
    public R<?> applyToAgree(@RequestParam("userAccount") String userAccount, @RequestParam("usernickname") String usernickname, @RequestParam("friendAccount") String friendAccount, @RequestParam("nickname") String nickname) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Integer userId = userInfoService.getUserInfoServiceByAccount(userAccount).getId();
            Integer friendId = userInfoService.getUserInfoServiceByAccount(friendAccount).getId();
            friendsInfoMapper.insert(FriendsInfo.createFriend(userId, friendId, nickname));
            friendsInfoMapper.insert(FriendsInfo.createFriend(friendId, userId, nickname));
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            return R.builder().code(ResponseEnum.RESPONSE_FAIL.getCode()).message(ResponseEnum.RESPONSE_FAIL.getMessage()).build();
        }
        return R.builder().code(ResponseEnum.RESPONSE_SUCCESS.getCode()).message(ResponseEnum.RESPONSE_SUCCESS.getMessage()).build();
    }

    /**
     * 获取好友详情
     *
     * @param friendsAccount
     * @return
     */
    @GetMapping("/info")
    public R<?> getFriendsInfo(@RequestParam("friendsAccount") String friendsAccount) {
        UserInfo userInfo = userInfoService.getUserInfoServiceByAccount(friendsAccount);
        if (ObjectUtil.isNull(userInfo)) {
            return BaseResponseUtils.getNotFoundResponse();
        }
        return BaseResponseUtils.getSuccessResponse(JSON.toJSONString(userInfo));
    }
}
