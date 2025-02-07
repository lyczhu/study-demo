package com.lawyus.study.proxy.bytebuddy;

public class RealObj {
    public void doSomething() {
        System.out.println("RealSubject doing something");
    }
    public void forward() {
        this.doSomething();
    }
}
