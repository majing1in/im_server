package com.xiaoma.im.listener.service;

import com.xiaoma.im.entity.MessageQueueList;

/**
 * @author ocean
 */
@FunctionalInterface
public interface LocalMessageSaveService {

    /**
     * 保存本地消息
     *
     * @param localMessage
     * @return
     */
    boolean saveLocalMessage(MessageQueueList localMessage) throws Exception;
}
