package org.qee.cloud.spring.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.qee.cloud.common.exceptions.CloudException;

@Aspect
@Slf4j
public class LogHandlerAspect {

    @Pointcut("execution(* org.qee.cloud..*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (CloudException e) {
            log.error("class :{},method:{},args:{}",
                    pjp.getTarget().getClass(),
                    pjp.getSignature().getName(),
                    pjp.getArgs(), e);
            throw e;
        }
    }

}
