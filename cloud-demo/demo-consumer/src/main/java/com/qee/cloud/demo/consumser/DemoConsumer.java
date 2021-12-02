package com.qee.cloud.demo.consumser;

import org.qee.cloud.spring.annotations.EnableCloud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class DemoConsumer {

    @Configuration
    @ComponentScan(basePackages = "com.qee.cloud.demo")
    @EnableCloud(scanPackages = "com.qee.cloud.demo", registryAddress = "zookeeper://127.0.0.1:2181", serviceRegistryPort = 20000, serviceRegistryProtocol = "cloud")
    static class Config {
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoConsumer.class, args);
        UserServiceHolder userServiceHolder = BeanContext.getBean(UserServiceHolder.class);
        for (int i = 0; i < 1000; i++) {
            userServiceHolder.print(i);
            System.out.println(i);
        }

    }

}
