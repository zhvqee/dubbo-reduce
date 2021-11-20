package org.qee.cloud.spring.test;

import org.junit.Test;
import org.qee.cloud.spring.processors.CloudReferenceBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

public class CloudReferenceBeanTest {


    @Configuration
    @ComponentScan(basePackages = "org.qee.cloud")
    static class Config {
        @Bean
        public CloudReferenceBeanPostProcessor cloudReferenceBeanPostProcessor() {
            return new CloudReferenceBeanPostProcessor();
        }

    }

    @Test
    public void testCloudReferenceBean() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        ConsumerComponent consumerComponent = applicationContext.getBean(ConsumerComponent.class);
        consumerComponent.print();
    }
}
