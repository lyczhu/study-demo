package com.lawyus.study.generic;

// HasF.java
public class HasF {
    public void f() {
        System.out.println("HasF.f()");
    }
}

// Manipulation.java
class Manipulator<T> {
    private T obj;

    public Manipulator(T x) {
        obj = x;
    }

    public void manipulate() {
        //obj.f();  // obj.f() 编译错误  泛型需要写成 T extends HasF
    }
}
