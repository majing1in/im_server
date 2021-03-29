package com.xiaoma.im.controller;

import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.UserMoneyMapper;
import com.xiaoma.im.entity.*;
import com.xiaoma.im.service.PackageService;
import com.xiaoma.im.utils.BaseResponseUtils;
import com.xiaoma.im.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author admin
 */
@RestController
@RequestMapping("/package")
public class PackageController {

    @Resource
    private UserMoneyMapper userMoneyMapper;

    @Resource
    private PackageService packageService;

    @PostMapping("/generator")
    public R<?> generatorPackage(@RequestBody RedPackage redPackage, @RequestParam("id") Integer id) {
        UserMoney userMoney = userMoneyMapper.getUserMoney("SELECT * from user_money WHERE uid = (SELECT id from user_info WHERE user_account = ' " + redPackage.getRedPackageOwner() + "')");
        Integer verificationData = packageService.verificationData(userMoney, redPackage);
        if (verificationData != 0) {
            return verificationData == 1 ? BaseResponseUtils.getFailedResponse("余额不足！") : BaseResponseUtils.getFailedResponse("红包数量太大！");
        }
        if (!packageService.deleteMoney(userMoney, userMoney.getMoney().subtract(redPackage.getAmount()))) {
            return BaseResponseUtils.getFailedResponse("生成红包失败！");
        }
        if (redPackage.getRedPackageType() == Constants.RED_PACKAGE_ONE) {
            return packageService.generatorSinglePackage(redPackage, id) ? BaseResponseUtils.getSuccessResponse() : BaseResponseUtils.getFailedResponse();
        }
        return packageService.generatorGroupPackage(redPackage, id) ? BaseResponseUtils.getSuccessResponse() : BaseResponseUtils.getFailedResponse();
    }


}
