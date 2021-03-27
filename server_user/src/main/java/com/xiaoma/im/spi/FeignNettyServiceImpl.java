package com.xiaoma.im.spi;

import cn.hutool.core.util.ObjectUtil;
import com.xiaoma.im.enums.ResponseEnum;
import com.xiaoma.im.feign.IFeignNettyService;
import com.xiaoma.im.utils.R;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Xiaoma
 * @Date 2021/2/8 0008 23:14
 * @Email 1468835254@qq.com
 */
@Component
public class FeignNettyServiceImpl {

    @Resource
    private IFeignNettyService feignNettyService;

    public String getChannelId(String userAccount) {
        R result = feignNettyService.getChannelId(userAccount);
        if (ObjectUtil.equals(result.getCode(), ResponseEnum.RESPONSE_SUCCESS.getCode())) {
            return (String) result.getData();
        }
        return null;
    }

    public boolean deleteChannelId(String userAccount) {
        R result = feignNettyService.deleteChannelId(userAccount);
        if (ObjectUtil.equals(result.getCode(), ResponseEnum.RESPONSE_SUCCESS.getCode())) {
            return true;
        }
        return false;
    }
}
