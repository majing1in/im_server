package com.xiaoma.im.controller;

import com.xiaoma.im.entity.MessagePackage;
import com.xiaoma.im.mq.producer.MessageContent;
import com.xiaoma.im.mq.producer.TransactionProducer;
import com.xiaoma.im.utils.UuidUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
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
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private TransactionProducer transactionProducer;

    @ApiOperation(value = "测试RocketMQ", notes = "测试RocketMQ")
    @GetMapping("/rocketMQ")
    public String testRocketMQ() {
        MessageContent content = MessageContent.getMessageContent(UuidUtils.getUuid(), MessagePackage.completePackage(1, "mq_topic_consumer".getBytes()));
        TransactionSendResult sendResult = transactionProducer.sendMessageInTransaction("mq_topic_consumer", content, () -> true);
        log.info("sendResult : {}", sendResult);
        return "success";
    }
}
