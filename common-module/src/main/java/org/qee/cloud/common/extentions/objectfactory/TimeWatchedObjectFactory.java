package org.qee.cloud.common.extentions.objectfactory;

import org.qee.cloud.common.annotations.AutoWraper;

@AutoWraper
public class TimeWatchedObjectFactory implements ObjectFactory {

    private ObjectFactory objectFactory;


    public TimeWatchedObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Override
    public Object getObject(String name, Class<?> clzz) {
        long s1 = System.currentTimeMillis();
        Object object = objectFactory.getObject(name, clzz);
        long s2 = System.currentTimeMillis();
        String format = String.format(objectFactory.getClass().getName() + ".getObject(%s,%s) cast time:%d", name, clzz.getSimpleName(), (s2 - s1));
        System.out.println(format);
        return object;
    }
}
