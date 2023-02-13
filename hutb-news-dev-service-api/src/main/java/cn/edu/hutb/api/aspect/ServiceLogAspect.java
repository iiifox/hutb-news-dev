package cn.edu.hutb.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ServiceLogAspect {

    /*
    AOP通知：
         1. 前置通知
         2. 后置通知
         3. 环绕通知
         4. 异常通知
         5. 最终通知
     */

    @Around("execution(* cn.edu.hutb.*.service.impl..*.*(..))")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("==== 开始执行 {}.{}====",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long takeTime = System.currentTimeMillis() - startTime;

        if (takeTime <= 2000) {
            log.info("当前执行耗时：{}", takeTime);
        } else if (takeTime <= 3000) {
            log.warn("当前执行耗时：{}", takeTime);
        } else {
            log.error("当前执行耗时：{}", takeTime);
        }
        return result;
    }

}
