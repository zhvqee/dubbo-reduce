package com.qee.cloud.demo.consumser;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class BeanContext implements InitializingBean, BeanFactoryAware {

    static BeanContext beanContext;

    private BeanFactory beanFactory;

    @Override
    public void afterPropertiesSet() throws Exception {

        this.beanFactory = beanFactory;
        beanContext = this;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public static <T> T getBean(String name) {
        return (T) beanContext.beanFactory.getBean(name);
    }

    public static <T> T getBean(Class<T> clzz) {
        return (T) beanContext.beanFactory.getBean(clzz);
    }
}
