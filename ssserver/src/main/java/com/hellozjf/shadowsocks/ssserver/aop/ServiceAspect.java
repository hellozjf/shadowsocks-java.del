package com.hellozjf.shadowsocks.ssserver.aop;

import com.hellozjf.shadowsocks.ssserver.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author hellozjf
 */
@Aspect
@Slf4j
@Component
public class ServiceAspect {

    @Pointcut("execution( public * com.hellozjf.shadowsocks.ssserver.service.impl.*.*(..))")
    public void servicePointCut() {
    }

    /**
     * 计算controller方法耗时，并将结果添加到返回的ResultVO对象中
     *
     * @param joinPoint
     */
    @Around("servicePointCut()")
    public Object timeAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object obj = joinPoint.proceed(joinPoint.getArgs());
        long costTime = System.currentTimeMillis() - startTime;
        String classAndMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        log.debug("执行{}耗时为：{}ms", classAndMethod, costTime);
        return obj;
    }
}
