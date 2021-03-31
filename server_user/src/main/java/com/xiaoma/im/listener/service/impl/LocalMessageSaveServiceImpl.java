package com.xiaoma.im.listener.service.impl;

import com.xiaoma.im.dao.MessageQueueListMapper;
import com.xiaoma.im.entity.MessageQueueList;
import com.xiaoma.im.listener.service.LocalMessageSaveService;
import com.xiaoma.im.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
public class LocalMessageSaveServiceImpl implements LocalMessageSaveService {

    @Resource
    private MessageQueueListMapper MessageQueueListMapper;

    @Override
    public boolean saveLocalMessage(MessageQueueList messageQueueList) throws Exception {

        int rs = MessageQueueListMapper.insert(SqlUtils.buildSqlForInsertMessage(messageQueueList.getMessageId(),
                messageQueueList.getIsConsumption(),
                messageQueueList.getMessageContent(),
                messageQueueList.getCreateTime(),
                messageQueueList.getUpdateTime()));
        if (rs != 1) {
            throw new Exception("save local message error");
        }
        return true;

    }
}
