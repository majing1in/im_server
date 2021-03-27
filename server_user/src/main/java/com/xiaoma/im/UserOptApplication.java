package com.xiaoma.im;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author Xiaoma
 * @Date 2021/2/6 0006 22:20
 * @Email 1468835254@qq.com
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan({"com.xiaoma.im.dao"})
@EnableDiscoveryClient
public class UserOptApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(UserOptApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
