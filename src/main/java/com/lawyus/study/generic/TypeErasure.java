package com.lawyus.study.generic;

/**
 * 泛型类型擦除
 * https://www.cnblogs.com/joeblackzqq/p/10813143.html
 *
 * https://www.jianshu.com/p/2bfbe041e6b7
 */
public interface TypeErasure<T> {

    T var(T t);
}

class TypeErasureSub implements TypeErasure<Integer> {

    @Override
    public Integer var(Integer integer) {
        return integer;
    }
}
