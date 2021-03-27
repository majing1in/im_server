package com.xiaoma.im.controller;

import cn.hutool.core.util.ObjectUtil;
import com.xiaoma.im.enums.ResponseEnum;
import com.xiaoma.im.feign.IFeignNettyService;
import com.xiaoma.im.req.PointRedEnvelope;
import com.xiaoma.im.utils.DateUtils;
import com.xiaoma.im.utils.R;
import com.xiaoma.im.utils.RedisTemplateUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/grab")
public class GrabRedEnvelopeController {

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Resource
    private IFeignNettyService iFeignNettyService;

    @PostMapping("/send/point/envelope")
    public R grabPointEnvelope(@RequestParam("uuid") String uuid, @RequestParam("type") String type, @RequestParam("amount") String amount, @RequestParam("message") String message, @RequestParam("userAccount") String userAccount) {
        PointRedEnvelope envelope = PointRedEnvelope.builder()
                .uuid(uuid)
                .type(type)
                .userAccount(userAccount)
                .message(message)
                .amount(amount)
                .createTime(DateUtils.localDateTimeConvertToDate()).build();
        // TODO 扣钱处理
        R result = iFeignNettyService.getChannelId(userAccount);
        if (!ObjectUtil.equals(result.getCode(), ResponseEnum.RESPONSE_SUCCESS.getCode())) {
            // TODO 用户不在线处理 加未读消息队列
        } else {
            // TODO 用户在线处理 直接进行转发，不会删除更改策略
        }
        return null;
    }
}
