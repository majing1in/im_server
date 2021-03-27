package com.xiaoma.im.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.UserInfoMapper;
import com.xiaoma.im.entity.UserInfo;
import com.xiaoma.im.spi.FeignNettyServiceImpl;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xiaoma.im.utils.*;
import com.xiaoma.im.enums.*;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * @Author Xiaoma
 * @Date 2021/2/6 0006 23:07
 * @Email 1468835254@qq.com
 */
@Api("用户登录模块API文档")
@Slf4j
@RestController
@RequestMapping("/user")
public class LoginController {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Resource
    private FeignNettyServiceImpl feignNettyServiceImpl;

    @ApiOperation("用户单点登录")
    @PostMapping("/login")
    public R userLogin(@RequestBody UserInfo userInfo) {
        if (ObjectUtil.isEmpty(userInfo) || StringUtils.isBlank(userInfo.getUserAccount()) || StringUtils.isBlank(userInfo.getUserPassword())) {
            return R.builder().code(ResponseEnum.RESPONSE_VALID.getCode()).message(ResponseEnum.RESPONSE_VALID.getMessage()).build();
        }
        UserInfo user = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserAccount, userInfo.getUserAccount())
                .and(item -> item.eq(UserInfo::getUserPassword, userInfo.getUserPassword())));
        if (ObjectUtil.isEmpty(user)) {
            return R.builder().code(ResponseEnum.RESPONSE_NOT_FIND.getCode()).message(ResponseEnum.RESPONSE_NOT_FIND.getMessage()).build();
        }
        String channelId = feignNettyServiceImpl.getChannelId(user.getUserAccount());
        String auth = (String) redisTemplateUtils.getObject(Constants.SERVER_ONLINE_AUTH + userInfo.getUserAccount());
        // 删除以前权限
        if (StringUtils.isNotEmpty(auth)) {
            redisTemplateUtils.deleteData(Collections.singletonList(Constants.SERVER_ONLINE_AUTH + userInfo.getUserAccount()));
        }
        // 获取通道id
        if (ObjectUtil.isNotEmpty(channelId)) {
            Map<String, Object> mapValue = redisTemplateUtils.getMapValue(channelId);
            NioSocketChannel channel = (NioSocketChannel) mapValue.get(Constants.OPTION_VALUE);
            channel.close();
            // 关闭通道，删除权限
            redisTemplateUtils.deleteData(Collections.singletonList(Constants.SERVER_ONLINE_AUTH + userInfo.getUserAccount()));
            if (!feignNettyServiceImpl.deleteChannelId(userInfo.getUserAccount())) {
                log.info("用户登录失败 ==> deleteChannelId ! account = {}， time = {}", userInfo.getUserAccount(), LocalDateTime.now());
                return R.builder().code(ResponseEnum.RESPONSE_FAIL.getCode()).message(ResponseEnum.RESPONSE_FAIL.getMessage()).build();
            }
        }
        String token = MD5Util.convertMD5(UuidUtils.getUuid());
        // 设置权限
        redisTemplateUtils.setObject(Constants.SERVER_ONLINE_AUTH + userInfo.getUserAccount(), token);
        log.info("用户登录成功! account = {}， time = {}", userInfo.getUserAccount(), LocalDateTime.now());
        return R.builder().code(ResponseEnum.RESPONSE_SUCCESS.getCode()).message(ResponseEnum.RESPONSE_SUCCESS.getMessage()).data(token).build();
    }

    @PostMapping("/online")
    public R userOnline(@RequestBody UserInfo userInfo, HttpServletRequest request) {

        return R.builder().code(ResponseEnum.RESPONSE_SUCCESS.getCode()).message(ResponseEnum.RESPONSE_SUCCESS.getMessage()).build();
    }
}
