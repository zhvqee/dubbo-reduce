package com.qee.cloud.demo.provider;

import org.qee.cloud.spring.annotations.EnableCloud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class DemoProvider {

    @Configuration
    @ComponentScan(basePackages = "com.qee.cloud.demo")
    @EnableCloud(scanPackages = "com.qee.cloud.demo", registryAddress = "zookeeper://127.0.0.1:2181")
    static class Config {
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoProvider.class, args);

    }

}
