package com.xiaoma.im.controller;

import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.dao.UserMoneyMapper;
import com.xiaoma.im.entity.*;
import com.xiaoma.im.service.PackageService;
import com.xiaoma.im.utils.BaseResponseUtils;
import com.xiaoma.im.utils.R;
import com.xiaoma.im.utils.RedisTemplateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @author admin
 */
@Api(tags = "红包模块")
@Slf4j
@RestController
@RequestMapping("/package")
public class PackageController {

    @Resource
    private UserMoneyMapper userMoneyMapper;

    @Resource
    private PackageService packageService;

    @Resource
    private PlatformTransactionManager transactionManager;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @ApiOperation(value = "生成红包", notes = "生成红包")
    @PostMapping("/generator")
    public R<?> generatorPackage(@RequestBody RedPackage redPackage, @RequestParam("id") Integer id) {
        redPackage.setList(new ArrayList<>());
        UserMoney userMoney = userMoneyMapper.getUserMoney("SELECT * from user_money WHERE uid = (SELECT id from user_info WHERE user_account = '" + redPackage.getRedPackageOwner() + "')");
        Integer verificationData = packageService.verificationData(userMoney, redPackage);
        if (verificationData != 0) {
            return verificationData == 1 ? BaseResponseUtils.getFailedResponse("余额不足！") : BaseResponseUtils.getFailedResponse("红包数量太大！");
        }
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            R<?> r;
            if (!packageService.updateMoney(userMoney, userMoney.getMoney().subtract(redPackage.getAmount()))) {
                return BaseResponseUtils.getFailedResponse("生成红包失败！");
            }
            if (redPackage.getRedPackageType() == Constants.RED_PACKAGE_ONE) {
                 r = packageService.generatorSinglePackage(redPackage, id) ? BaseResponseUtils.getSuccessResponse() : BaseResponseUtils.getFailedResponse();
            } else {
                r = packageService.generatorGroupPackage(redPackage, id) ? BaseResponseUtils.getSuccessResponse() : BaseResponseUtils.getFailedResponse();
            }
            transactionManager.commit(status);
            return r;
        } catch (Exception e) {
            log.error("生成红包发生异常！", e);
            transactionManager.rollback(status);
            return BaseResponseUtils.getFailedResponse();
        }
    }

    @ApiOperation(value = "个人红包", notes = "个人红包")
    @GetMapping("/getFromFriend")
    public R<?> getSinglePackage(@RequestParam("redPackageId") String redPackageId, @RequestParam("account") String account) {
        return packageService.getSinglePackage(redPackageId, account) ? BaseResponseUtils.getSuccessResponse() : BaseResponseUtils.getFailedResponse();
    }

    @ApiOperation(value = "抢红包", notes = "抢红包")
    @GetMapping("/getFromGroup")
    public R<?> getGroupPackage(@RequestParam("redPackageId") String redPackageId, @RequestParam("account") String account) {
        return packageService.getGroupPackage(redPackageId, account) ? BaseResponseUtils.getSuccessResponse() : BaseResponseUtils.getFailedResponse();
    }
}
