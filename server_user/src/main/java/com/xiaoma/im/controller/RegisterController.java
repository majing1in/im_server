package com.xiaoma.im.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.UserInfoMapper;
import com.xiaoma.im.entity.UserInfo;
import com.xiaoma.im.utils.BaseResponseUtils;
import com.xiaoma.im.utils.DateUtils;
import com.xiaoma.im.utils.R;
import com.xiaoma.im.utils.RedisTemplateUtils;
import com.xiaoma.im.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * @Author Xiaoma
 * @Date 2021/2/6 0006 23:47
 * @Email 1468835254@qq.com
 */

@Api("用户注册模块API文档")
@Slf4j
@RestController
@RequestMapping("/user")
public class RegisterController {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Resource
    private PlatformTransactionManager platformTransactionManager;

    /**
     * 客户端进入登录页面后，生成过期时间为5分钟的验证码
     *
     * @param
     * @return
     */
    @ApiOperation("生成注册验证码")
    @GetMapping("/auth")
    public R<?> userAuth(HttpServletRequest request) {
        String uuid = request.getHeader("uuid");
        if (StringUtils.isBlank(uuid)) {
            return BaseResponseUtils.getFailedResponse();
        }
        // 生成验证码
        String random = String.valueOf((int) (Math.random() * 9000 + 1000));
        // 设置过期时间 5 分钟
        long expireTime = 5 * 60;
        redisTemplateUtils.setObject(Constants.SERVER_REGISTER_TIMEOUT + uuid, random, expireTime);
        return BaseResponseUtils.getSuccessResponse(random);
    }

    @ApiOperation("注册用户")
    @PostMapping("/register")
    public R<?> userRegister(@RequestBody UserInfoVo userInfoVo, HttpServletRequest request) {
        if (ObjectUtil.isEmpty(userInfoVo) || StringUtils.isBlank(userInfoVo.getUserAccount()) || StringUtils.isBlank(userInfoVo.getUserPassword()) || StringUtils.isBlank(userInfoVo.getVerificationCode())) {
            return BaseResponseUtils.getValidResponse();
        }
        String uuid = request.getHeader("uuid");
        if (StringUtils.isBlank(uuid)) {
            return BaseResponseUtils.getValidResponse();
        }
        String redisKey = Constants.SERVER_REGISTER_TIMEOUT + uuid;
        String verificationCode = (String) redisTemplateUtils.getObject(redisKey);
        if (StringUtils.isBlank(verificationCode) || !ObjectUtil.equal(verificationCode, userInfoVo.getVerificationCode())) {
            return BaseResponseUtils.getTimeoutResponse();
        }
        redisTemplateUtils.deleteData(Collections.singletonList(redisKey));
        UserInfo user = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUserAccount, userInfoVo.getUserAccount()));
        if (ObjectUtil.isNotEmpty(user)) {
            return BaseResponseUtils.getFailedResponse();
        }
        userInfoVo.setCreateTime(DateUtils.localDateTimeConvertToDate());
        userInfoVo.setUpdateTime(DateUtils.localDateTimeConvertToDate());
        TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            platformTransactionManager.commit(status);
            return BaseResponseUtils.getSuccessResponse();
        } catch (Exception e) {
            platformTransactionManager.rollback(status);
            return BaseResponseUtils.getUnknownResponse();
        }
    }
}
