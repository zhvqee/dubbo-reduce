package org.qee.cloud.spring.objectfactory;

import org.qee.cloud.common.extentions.objectfactory.ObjectFactory;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class SpringObjectFactory implements ObjectFactory {

    private List<ApplicationContext> applicationContextList = new ArrayList<>();

    public void addApplicationContextList(List<ApplicationContext> applicationContextList) {
        applicationContextList.addAll(applicationContextList);
    }

    @Override
    public Object getObject(String name, Class<?> clzz) {
        for (ApplicationContext applicationContext : applicationContextList) {
            Object bean = applicationContext.getBean(name);
            if (bean != null) {
                return bean;
            }
        }
        for (ApplicationContext applicationContext : applicationContextList) {
            Object bean = applicationContext.getBean(clzz);
            if (bean != null) {
                return bean;
            }
        }
        return null;
    }
}
