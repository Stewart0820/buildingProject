package com.stewart.building.test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Stewart
 * @create 2022/4/11
 * @Description
 */
public class Demo1 {
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("abc", "def", "gkh", "abc");
        Stream<String> stream = strings.stream().filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                System.out.println("执行");
                return true;
            }
        });
        System.out.println("11");
        stream.count();
        for(String s:strings){
            System.out.println(s);
        }

    }
}
