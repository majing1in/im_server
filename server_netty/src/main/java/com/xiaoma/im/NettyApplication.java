package com.xiaoma.im;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author Xiaoma
 * @Date 2021/2/7 0007 23:56
 * @Email 1468835254@qq.com
 */


@EnableAsync
@MapperScan({"com.xiaoma.im.dao"})
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class NettyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);

    }
}
