package com.hellozjf.shadowsocks.ssserver.aop;

import com.hellozjf.shadowsocks.ssserver.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Jingfeng Zhou
 */
@Aspect
@Slf4j
@Component
public class ControllerAspect {

    @Pointcut("execution( * com.hellozjf.shadowsocks.ssserver.controller..*.*(..))")
    public void controllerPointCut() {
    }

    /**
     * 计算controller方法耗时，并将结果添加到返回的ResultVO对象中
     *
     * @param joinPoint
     */
    @Around("controllerPointCut()")
    public Object timeAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object obj = joinPoint.proceed(joinPoint.getArgs());
        long costTime = System.currentTimeMillis() - startTime;
        String classAndMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        log.debug("执行{}耗时为：{}ms", classAndMethod, costTime);
        // 对controller中返回的ResultVO对象增加costTime字段
        if (obj instanceof ResultVO) {
            ResultVO resultVO = (ResultVO) obj;
            resultVO.setCostTime(costTime);
            return resultVO;
        }
        return obj;
    }
}
