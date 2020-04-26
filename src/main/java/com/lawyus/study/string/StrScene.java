package com.lawyus.study.string;

/**
 * describe please!
 *
 * @author lyc
 * @since 2020/4/26
 */
public class StrScene {

    public static void main(String[] args) {
        str2();
    }

    public static void str() {
        String str1 = new String("abc");
        String str2 = "abc";

        System.out.println(str1 == str2);
        System.out.println(str1.equals(str2));
    }

    public static void str2() {
        String str1 = "ab";
        String str2 = "abc";
        String str3 = str1 + "c";
        System.out.println(str2 == str3);
        System.out.println(str2.equals(str3));
    }
}
