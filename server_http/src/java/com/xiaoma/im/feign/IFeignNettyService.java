package com.xiaoma.im.feign;

import com.xiaoma.im.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 22:58
 * @Email 1468835254@qq.com
 */
@FeignClient(name = "server-netty")
public interface IFeignNettyService {

    @GetMapping("/netty/channelId")
    public R<?> getChannelId(@RequestParam("userAccount") String userAccount);

    @GetMapping("/netty/deleteChannelId")
    public R<?> deleteChannelId(@RequestParam("userAccount") String userAccount);
}
