package com.lawyus.study;

/**
 * 在类继承中实例变量获取到的实例值验证
 * 实例变量值 与声明的类型有关，而不是创建的类型
 *
 * @author: lyc
 * @date: 2023/12/16
 */
public class Test {
    public static void main(String[] args) {
        ParentClass parentClass = new SubClass();
        SubClass subClass = new SubClass();
        System.out.println(parentClass.i + subClass.i);
    }
}

class ParentClass {
    public int i = 10;
}

class SubClass extends ParentClass {
    public int i = 30;
}
