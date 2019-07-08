package com.dj.p2p.wind;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.dj.p2p.wind.mapper")
@EnableTransactionManagement
//开启定时任务
@EnableScheduling
public class WindStart {

    public static void main(String[] args) {
        SpringApplication.run(WindStart.class, args);
    }
}