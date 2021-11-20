package org.qee.cloud.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.common.annotations
 * @ClassName: AutoWraper
 * @Description:
 * @Date: 2021/11/19 4:52 下午
 * @Version: 1.0
 */

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoWraper {
    int order() default 0;
}
