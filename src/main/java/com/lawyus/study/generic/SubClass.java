package com.lawyus.study.generic;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;

public class SubClass extends BaseClass {

    public static void main(String[] args) {
        List<? extends BaseClass> list = Arrays.asList(new SubClass(), new SubClass(), new Sub2());
        SubClass sc = new SubClass();
        sc.setId(1);
        list.forEach(System.out::println);
        //list.add(sc);
        //SuperBaseClass bc = list.get(1);
    }
}

class Sub2 extends BaseClass {}

@EqualsAndHashCode(callSuper = true)
@Data
class BaseClass extends SuperBaseClass {
    private Integer id;
}

class SuperBaseClass {
}

