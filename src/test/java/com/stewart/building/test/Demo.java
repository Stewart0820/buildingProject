package com.stewart.building.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Stewart
 * @create 2022/4/11
 * @Description
 */
public class Demo {
    public static void main(String[] args) {
//        List<String> strings = Arrays.asList("1", "2", "3");
//        strings.forEach(System.out::println);

        List<String> strings = Arrays.asList("abc", "def", "gkh", "abc");
        //过滤，只保留abc
        Stream<String> stringStream = strings.stream().filter("abc"::equals);
        stringStream.forEach(System.out::println);

        Stream<String> limit = strings.stream().limit(1);
        String[] array = limit.toArray(String[]::new);

    }

}
