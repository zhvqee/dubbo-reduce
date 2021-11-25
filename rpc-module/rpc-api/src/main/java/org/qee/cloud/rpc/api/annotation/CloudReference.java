package org.qee.cloud.rpc.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 远程服务引用注解
 * 类比  <dubbo:reference id="demoService4" interface="org.apache.dubbo.demo.service.DemoService" version="2.0.0" group="*"  />
 */

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudReference {

    /**
     * id ,比如在Spring 中的Bean Id,Name
     * 可以通过id 从Spring 容器中拿远程代理对象
     *
     * @return
     */
    String id() default "";

    // 个人感觉不需要 这个 2个值，当CloudReference 标注在某个属性时，就是要引用该类型的远程服务
    /**
     * 接口 全限定名
     *
     * @return
     */
    //  String interfaceName();

    /**
     * 接口 Class 如果指定，interfaceName 忽略
     * @return
     */
    // Class<?> interfaceClass();

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


    String mock() default "";

    boolean check() default true;


}
