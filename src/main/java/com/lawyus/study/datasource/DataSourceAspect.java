package com.lawyus.study.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceAspect {

    // 切入点：匹配所有标注了@DataSource注解的方法
    @Pointcut("@annotation(com.lawyus.study.datasource.v1.DataSource)")
    public void dataSourcePointCut() {}

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 获取注解上的数据源标识
        DataSource dataSource = method.getAnnotation(DataSource.class);
        if (dataSource != null) {
            DynamicDataSourceContextHolder.setDataSourceKey(dataSource.value());
        }

        try {
            // 执行目标方法
            return point.proceed();
        } finally {
            // 清除数据源标识（必须finally，防止线程复用导致的问题）
            DynamicDataSourceContextHolder.clearDataSourceKey();
        }
    }
}
