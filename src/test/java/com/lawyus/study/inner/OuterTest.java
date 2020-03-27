package com.lawyus.study.inner;

import org.junit.Test;

import static org.junit.Assert.*;

public class OuterTest {

    @Test
    public void genInstance() {
        Outer.StaticInner staticInner = new Outer.StaticInner();

        Outer outer = new Outer();
        Outer.normalInner normalInner = outer.new normalInner();
    }

}