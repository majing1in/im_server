package com.xiaoma.im.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.xiaoma.im.entity.UserInfo;
import com.xiaoma.im.service.UserInfoService;
import com.xiaoma.im.utils.BaseResponseUtils;
import com.xiaoma.im.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 22:20
 * @Email 1468835254@qq.com
 */
@Api("用户信息模块")
@RestController
@RequestMapping("/user")
public class AboutUserController {

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("获取用户个人详情")
    @GetMapping("/info")
    public R<?> getUserInfo(@RequestParam("id") Integer id) {
        UserInfo userInfo = this.userInfoService.getUserInfoServiceById(id);
        if (ObjectUtil.isEmpty(userInfo)) {
            return BaseResponseUtils.getNotFoundResponse();
        }
        return BaseResponseUtils.getSuccessResponse(JSON.toJSONString(userInfo));
    }

    @ApiOperation("修改用户个人详情")
    @GetMapping("/update")
    public R<?> updateUserInfo(@RequestBody UserInfo userInfo) {
        boolean result = userInfoService.updateUserInfo(userInfo);
        if (!result) {
            return BaseResponseUtils.getFailedResponse();
        }
        return BaseResponseUtils.getSuccessResponse();
    }
}
