package com.xiaoma.im.listener;

import com.xiaoma.im.dao.MessageQueueListMapper;
import com.xiaoma.im.entity.MessageQueueList;
import com.xiaoma.im.listener.service.LocalMessageSaveService;
import com.xiaoma.im.mq.producer.MessageContent;
import com.xiaoma.im.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@RocketMQTransactionListener
public class LocalTransactionListener implements RocketMQLocalTransactionListener {

    @Resource
    private TransactionContainer transactionContainer;
    @Resource
    private MessageQueueListMapper messageQueueListMapper;
    @Resource
    private LocalMessageSaveService localMessageSaveService;

    /**
     * 执行本地事务
     *
     * @param message
     * @param o
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        String mqTxId = (String) message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        try {
            Boolean executeOk = transactionContainer.execute(mqTxId);
            MessageQueueList messageQueueList = (MessageQueueList) o;
            Boolean isOk = localMessageSaveService.saveLocalMessage(messageQueueList);
            if (null != executeOk && executeOk && isOk) {
                log.info("mqTxId={},本地事务执行成功", mqTxId);
                return RocketMQLocalTransactionState.COMMIT;
            }

            throw new Exception("execute local transaction error");
        } catch (Exception e) {
            log.error("error occurred execute local transaction", e);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 提供给事务执行状态检查的回调方法，给broker用的(异步回调）,如果回查失败，消息就丢弃
     *
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageContent messageContent = (MessageContent) message.getPayload();
        long rs = messageQueueListMapper.countByTransactionNo(SqlUtils.buildSqlForUpdateMessage(messageContent.getMessageId()));
        if (rs > 0) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
