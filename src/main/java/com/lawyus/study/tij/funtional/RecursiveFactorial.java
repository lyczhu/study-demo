package com.lawyus.study.tij.funtional;

/**
 * TODO describe please!
 *
 * 递归函数是一个自我调用的函数。可以编写递归的 Lambda 表达式，但需要注意：递归方法必须是实例变量或静态变量，否则会出现编译时错误。
 * Variable must have been initialized
 *
 * @author yushing
 * @since 2020/4/4
 */

// 递归方法 Lambda表达式 静态变量形式
public class RecursiveFactorial {
    static IntCall fact;
    public static void main(String[] args) {
        fact = n -> n == 0 ? 1 : n * fact.call(n - 1);
        for(int i = 0; i <= 10; i++)
            System.out.println(fact.call(i));
    }
}

// 递归方法 Lambda表达式 实例变量形式
class RecursiveFibonacci {
    IntCall fib;

    RecursiveFibonacci() {
        fib = n -> n == 0 ? 0 :
                n == 1 ? 1 :
                        fib.call(n - 1) + fib.call(n - 2);
    }

    int fibonacci(int n) { return fib.call(n); }

    public static void main(String[] args) {
        RecursiveFibonacci rf = new RecursiveFibonacci();
        for(int i = 0; i <= 10; i++)
            System.out.println(rf.fibonacci(i));
    }
}


interface IntCall {
    int call(int arg);
}

class Recursive2 {
    IntCall call;

    public void testCall() {
        call = n -> n == 0 ? 0 :
                n == 1 ? 1 :
                        call.call(n - 1) + call.call(n - 2);
    }

    public void testCall2(int n) {
        call.call(n);
    }

    public static void main(String[] args) {
        
    }
}
