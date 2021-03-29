package com.xiaoma.im.utils;

import cn.hutool.core.util.ObjectUtil;
import com.xiaoma.im.constants.Constants;
import com.xiaoma.im.entity.UserStatus;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 12:53
 * @Email 1468835254@qq.com
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
public class SessionSocketUtils {

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    /**
     * 保存用户与channel的关系
     *
     * @param userAccount
     * @param nioSocketChannel
     */
    public boolean saveSession(String userAccount, NioSocketChannel nioSocketChannel) {
        UserStatus userStatus = UserStatus.builder().channel(nioSocketChannel).loginTime(DateUtils.localDateTimeConvertToDate()).account(userAccount).build();
        return redisTemplateUtils.putIfAbsent(Constants.SERVER_REDIS_LIST, Constants.SERVER_ONLINE + userAccount, userStatus);
    }

    /**
     * 移除关系
     *
     * @param userAccount
     */
    public boolean removeSessionByAccount(String userAccount) {
        return redisTemplateUtils.delete(Constants.SERVER_REDIS_LIST, Constants.SERVER_ONLINE + userAccount) > 0;
    }

    public NioSocketChannel getUserNioSocketChannelByAccount(String userAccount) {
        UserStatus userStatus = (UserStatus)redisTemplateUtils.getHashKey(Constants.SERVER_REDIS_LIST, Constants.SERVER_ONLINE + userAccount);
        if (ObjectUtil.isNull(userStatus)) {
            return null;
        }
        return userStatus.getChannel();
    }

    public UserStatus getUserStatusByAccount(String userAccount) {
        UserStatus userStatus = (UserStatus)redisTemplateUtils.getHashKey(Constants.SERVER_REDIS_LIST, Constants.SERVER_ONLINE + userAccount);
        if (ObjectUtil.isNull(userStatus)) {
            return null;
        }
        return userStatus;
    }

    public UserStatus getUserStatusById(String channelId) {
        Map<Object, Object> map = redisTemplateUtils.getHashEntries(Constants.SERVER_REDIS_LIST);
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            UserStatus userStatus = (UserStatus) entry;
            if (ObjectUtil.equals(((UserStatus) entry).getChannel().id().asLongText(), channelId)) {
                return userStatus;
            }
        }
        return null;
    }

}
