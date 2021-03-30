package com.xiaoma.im.controller;

import com.alibaba.fastjson.JSON;
import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.mq.producer.MessageContent;
import com.xiaoma.im.mq.producer.RocketMqProducer;
import com.xiaoma.im.utils.UuidUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author Xiaoma
 * @Date 2021/3/30 0030 21:52
 * @Email 1468835254@qq.com
 */
@Api(tags = "测试类")
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private RocketMqProducer rocketMqProducer;

    @ApiOperation(value = "测试RocketMQ", notes = "测试RocketMQ")
    @GetMapping("/rocketMQ")
    public String testRocketMQ() {
        GenericMessage<MessageContent> content = MessageContent.getMessageContent(UuidUtils.getUuid(), MessagePackage.completePackage(1, "RocketMQ".getBytes()));
        rocketMqProducer.asyncSend(content, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(JSON.toJSONString(sendResult));
            }
            @Override
            public void onException(Throwable throwable) {
                System.out.println(JSON.toJSONString(throwable));
            }
        });
        return "success";
    }
}
