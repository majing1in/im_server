package com.xiaoma.im.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.UserInformationMapper;
import com.xiaoma.im.dao.UserLoginRecordMapper;
import com.xiaoma.im.entity.UserInformation;
import com.xiaoma.im.entity.UserLoginRecord;
import com.xiaoma.im.entity.UserStatus;
import com.xiaoma.im.spi.FeignNettyServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.xiaoma.im.utils.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

/**
 * @Author Xiaoma
 * @Date 2021/2/6 0006 23:07
 * @Email 1468835254@qq.com
 */
@SuppressWarnings("unchecked")
@Api("用户登录模块API文档")
@Slf4j
@RestController
@RequestMapping("/user")
public class LoginController {

    @Resource
    private UserInformationMapper userInformationMapper;

    @Resource
    private UserLoginRecordMapper loginRecordMapper;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Resource
    private FeignNettyServiceImpl feignNettyServiceImpl;

    @ApiOperation("用户单点登录")
    @PostMapping("/login")
    public R<?> userLogin(@RequestBody UserInformation userInformation, HttpServletRequest request) {
        if (ObjectUtil.isEmpty(userInformation) || StringUtils.isBlank(userInformation.getUserAccount()) || StringUtils.isBlank(userInformation.getUserPassword())) {
            return BaseResponseUtils.getValidResponse();
        }
        UserInformation user = userInformationMapper.selectOne(new LambdaQueryWrapper<UserInformation>()
                .eq(UserInformation::getUserAccount, userInformation.getUserAccount())
                .and(item -> item.eq(UserInformation::getUserPassword, userInformation.getUserPassword())));
        if (ObjectUtil.isEmpty(user)) {
            return BaseResponseUtils.getNotFoundResponse();
        }
        String channelId = feignNettyServiceImpl.getChannelId(user.getUserAccount());
        String auth = (String) redisTemplateUtils.get(Constants.SERVER_ONLINE_AUTH + userInformation.getUserAccount());
        // 删除以前权限
        if (StringUtils.isNotEmpty(auth)) {
            redisTemplateUtils.delete(Collections.singletonList(Constants.SERVER_ONLINE_AUTH + userInformation.getUserAccount()));
        }
        // 获取通道id
        if (ObjectUtil.isNotEmpty(channelId)) {
            UserStatus userStatus = (UserStatus) redisTemplateUtils.getHashKey(Constants.SERVER_REDIS_LIST ,Constants.SERVER_ONLINE + userInformation.getUserAccount());
            userStatus.getChannel().close();
            // 关闭通道，删除权限
            redisTemplateUtils.delete(Collections.singletonList(Constants.SERVER_ONLINE_AUTH + userInformation.getUserAccount()));
            if (!feignNettyServiceImpl.deleteChannelId(userInformation.getUserAccount())) {
                log.info("用户登录失败 ==> deleteChannelId ! account = {}， time = {}", userInformation.getUserAccount(), LocalDateTime.now());
                return BaseResponseUtils.getFailedResponse();
            }
        }
        String token = MD5Util.convertMD5(UuidUtils.getUuid());
        // 设置权限
        redisTemplateUtils.set(Constants.SERVER_ONLINE_AUTH + userInformation.getUserAccount(), token);
        UserLoginRecord loginRecord = UserLoginRecord.builder().userId(user.getId()).loginTime(DateUtils.localDateTimeConvertToDate()).userIp(request.getRemoteAddr()).build();
        loginRecordMapper.insert(loginRecord);
        log.info("用户登录成功! account = {}， time = {}", userInformation.getUserAccount(), LocalDateTime.now());
        return BaseResponseUtils.getSuccessResponse(token);
    }

    /**
     * 当客户端异常关闭前发送请求
     *
     * @param userAccount
     * @return
     */
    @GetMapping("/online")
    public R<?> userOnline(@RequestParam("userAccount") String userAccount) {
        // 直接删除key
        if (feignNettyServiceImpl.deleteChannelId(userAccount)) {
            return BaseResponseUtils.getSuccessResponse();
        }
        // 删除失败策略
        String channelId = feignNettyServiceImpl.getChannelId(userAccount);
        redisTemplateUtils.delete(Collections.singletonList(channelId));
        return BaseResponseUtils.getSuccessResponse();
    }

}
