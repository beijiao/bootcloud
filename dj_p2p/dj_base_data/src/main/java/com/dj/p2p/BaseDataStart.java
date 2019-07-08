package com.dj.p2p;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.dj.p2p.mapper")
@EnableTransactionManagement
public class BaseDataStart {

    public static void main(String[] args) {
        SpringApplication.run(BaseDataStart.class, args);
    }
}