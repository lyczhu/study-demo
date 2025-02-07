package com.lawyus.study.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.FixedValue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class ByteBuddyAopExample {
    public static void main(String[] args) throws Exception {
        String methodName = "doSomething";
        save(methodName);
    }

    private static void intercept(String methodName)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<? extends RealObj> dynamicType = new ByteBuddy()
                .subclass(RealObj.class) // 指定要拦截的类
                .name("com.lawyus.bytebuddy.RealObjGeneratedClass")
                .method(named(methodName)) // 指定要拦截的方法
                .intercept(Advice.to(LoggerAdvisor.class)) // 应用拦截器
                .make()
                .load(RealObj.class.getClassLoader())
                .getLoaded();

        // 创建动态生成的类的实例
        Object proxyInstance = dynamicType.newInstance();
        // 调用方法，将触发拦截器
        proxyInstance.getClass().getMethod(methodName).invoke(proxyInstance);
    }

    public static void save(String methodName) throws IOException {
        new ByteBuddy()
                .subclass(RealObj.class) // 指定要拦截的类
                .name("com.lawyus.bytebuddy.RealObjForwardClass")
                .method(named(methodName)) // 指定要拦截的方法
                .intercept(Advice.to(LoggerAdvisor.class)) // 应用拦截器
                .method(named("forward"))
                .intercept(Advice.to(LoggerAdvisor.class))
                .make()
                .saveIn(new File("output")); // 保存生成的类文件到 output 目录
    }
}
