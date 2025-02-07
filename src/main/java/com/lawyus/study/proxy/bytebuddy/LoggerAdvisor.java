package com.lawyus.study.proxy.bytebuddy;

import net.bytebuddy.asm.Advice;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LoggerAdvisor {
    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments) {
        System.out.println("Enter " + method.getName() + " with arguments: " + Arrays.toString(arguments));
    }

    @Advice.OnMethodExit
    public static void onMethodExit(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments) {
        System.out.println("Enter " + method.getName() + " with arguments: " + Arrays.toString(arguments));
    }

    /*@Advice.OnMethodExit
    public static void onMethodExit(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments, @Advice.Return Object ret) {
        if ("doSomething".equals(method.getName())) {
            System.out.println("Enter " + method.getName() + " with arguments: " + Arrays.toString(arguments));
        }
    }*/
}
