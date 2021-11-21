package org.qee.cloud.spring.test;

import org.junit.Test;
import org.qee.cloud.spring.annotations.EnableCloud;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

public class EnableCloudTest {

    @Configuration
    @ComponentScan(basePackages = "org.qee.cloud")
    @EnableCloud(scanPackages = "org.qee.cloud")
    static class Config {
    }

    @Test
    public void testCloudReferenceBean() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(EnableCloudTest.Config.class);
        ConsumerComponent consumerComponent = applicationContext.getBean(ConsumerComponent.class);
        // consumerComponent.print();

        UserService userServiceImplCloudService = (UserService) applicationContext.getBean("userServiceImplCloudService");
        System.out.println(userServiceImplCloudService);

        Object contextBean = applicationContext.getBean("org.qee.cloud.spring.test.UserService:*:*");
        System.out.println(contextBean);
    }
}
