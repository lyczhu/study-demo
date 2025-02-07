package com.lawyus.study.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class CglibProxyExample {
    public static void main(String[] args) {
        CglibProxyExample cglibProxyExample = new CglibProxyExample();
        RealObj proxyObj = cglibProxyExample.createProxy();
        proxyObj.forward();
    }

    RealObj createProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealObj.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            Object result = null;
            if ("doSomething".equals(method.getName())) {
                System.out.println("before doSomething method execution");
                result = proxy.invokeSuper(obj, args);
                System.out.println("after doSomething method execution");
            }
            return result;
        });
        return (RealObj) enhancer.create();
    }
}

class RealObj {
    public void doSomething() {
        System.out.println("RealSubject doing something");
    }
    public void forward() {
        this.doSomething();
    }
}


