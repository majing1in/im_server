package com.xiaoma.im.utils;

import cn.hutool.core.util.ObjectUtil;
import com.xiaoma.im.constants.Constants;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 12:53
 * @Email 1468835254@qq.com
 */
@Slf4j
@Component
public class SessionSocketUtils {

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    private final Map<String, NioSocketChannel> sessionMap = new ConcurrentHashMap<String, NioSocketChannel>();

    public Map<String, NioSocketChannel> getSessionMap() {
        return this.sessionMap;
    }

    public NioSocketChannel getNioSocketChannel(String userAccount) {
        return sessionMap.get(userAccount);
    }

    public NioSocketChannel removeSessionMap(String userAccount) {
        return sessionMap.remove(userAccount);
    }

    /**
     * 保存用户与channel的关系
     * @param userAccount
     * @param nioSocketChannel
     */
    public void saveSession(String userAccount, NioSocketChannel nioSocketChannel) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.OPTION_KEY, userAccount);
        map.put(Constants.OPTION_VALUE, nioSocketChannel);
        redisTemplateUtils.setKey(Constants.SERVER_ONLINE + nioSocketChannel.id().asLongText(), map);
        sessionMap.put(userAccount, nioSocketChannel);
    }

    /**
     * 移除关系
     * @param channelId
     */
    public void removeSession(String channelId) {
        if (StringUtils.isBlank(channelId)) {
            return;
        }
        Map<String, Object> mapValue = redisTemplateUtils.getMapValue(channelId);
        if (ObjectUtil.isNotEmpty(mapValue)) {
            redisTemplateUtils.deleteData(Collections.singletonList(channelId));
            sessionMap.remove((String) mapValue.get(Constants.OPTION_KEY));
        }
    }

    /**
     * 清空关系
     */
    public void clearSession() {
        Set<Map.Entry<String, NioSocketChannel>> entries = sessionMap.entrySet();
        for (Map.Entry<String, NioSocketChannel> entry : entries) {
            String channelId = entry.getValue().id().asLongText();
            this.removeSession(channelId);
        }
        sessionMap.clear();
    }

}
