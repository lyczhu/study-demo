package com.lawyus.study.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 日志配置切面
 *
 * @author: lyc
 * @date: 2024/3/12
 */
@Aspect
@Component
public class LoggingAspect {
    // 定义日志记录的切入点
    @Pointcut("execution(* com.lawyus.study.aspectj.service.*.*(..))")
    public void logPointcut() {
    }

    // 前置通知：在方法执行前执行
    @Before("logPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        // 日志记录逻辑
        System.out.println("logBefore");
    }

    // 后置通知：在方法执行后执行
    @After("logPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        // 日志记录逻辑
        System.out.println("logAfter");
    }

    // 环绕通知：可以包围方法执行的前后
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 方法执行前的逻辑
        IO.println("logAround:before");

        Object result = proceedingJoinPoint.proceed();

        // 方法执行后的逻辑
        IO.println("logAround:after");
        return result;
    }
}
