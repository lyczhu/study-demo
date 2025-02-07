package com.lawyus.study.annotation;    // PackageElement

@CheckGetter
public class AnnotatedClass {           // TypeElement
    int a;                              // VariableElement
    static int b;                       // VariableElement
    AnnotatedClass () {}                // ExecutableElement
    void setA (                         // ExecutableElement
            int newA                    // VariableElement
    ) {}
}
