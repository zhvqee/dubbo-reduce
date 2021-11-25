package com.qee.cloud.demo.provider;

import org.qee.cloud.spring.annotations.EnableCloud;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

public class DemoProvider {

    @Configuration
    @ComponentScan(basePackages = "com.qee.cloud.demo")
    @EnableCloud(scanPackages = "com.qee.cloud.demo")
    static class Config {
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoProvider.class, args);

    }

}
