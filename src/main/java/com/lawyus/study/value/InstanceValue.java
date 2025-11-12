package com.lawyus.study.value;

/**
 * 字段隐藏（field hiding）机制，两个value字段的值在内存中同时存在
 * 实例变量值 与实际对象的类型无关，而与引用变量的声明类型有关
 *
 * @author: lyc
 * @date: 2023/12/16
 */
public class InstanceValue {
    public static void main(String[] args) {
        ParentClass parentClass = new SubClass();
        SubClass subClass = new SubClass();
        System.out.println("ParentClass.value: " + parentClass.value); // 输出 5
        System.out.println("SubClass.value: " + subClass.value);  // 输出 15
    }
}

class ParentClass {
    public int value = 5;
}

class SubClass extends ParentClass {
    public int value = 15;
}