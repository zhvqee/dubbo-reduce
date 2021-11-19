package org.qee.cloud.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * dubbo 这里可以修饰class,如果修饰class 说明是自适应类，这里改为method ,自适应类改为配置文件中的
 * org.qee.cloud.common.extentions.Adpative 中key =default
 *
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.common.annotations
 * @ClassName: Adaptive
 * @Description:
 * @Date: 2021/11/19 2:08 下午
 * @Version: 1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Adaptive {

    String[] keys();
}
