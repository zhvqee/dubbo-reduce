package org.qee.cloud.rpc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务提供者暴露
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudService {

    /**
     * 需要导出的服务，不配置的话，就是注解上的类的接口
     *
     * @return
     */
    Class<?> interfaceClass() default void.class;

    String id();

    /**
     * 引用的组
     *
     * @return
     */
    String group() default "*";

    /**
     * 引用的版本
     *
     * @return
     */
    String version() default "*";

}
