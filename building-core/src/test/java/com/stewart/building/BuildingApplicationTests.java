package com.stewart.building;

import cn.hutool.core.lang.Console;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.net.www.content.text.Generic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BuildingApplicationTests {

    @Test
    public void contextLoads() throws Exception {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        Class<? extends ArrayList> clazz = list.getClass();
        Method add = clazz.getDeclaredMethod("add", Object.class);
        add.invoke(list,"ke");
        Console.log(list);
    }

    @Test
    public void text(){
        List<String> strings = Arrays.asList("1", "2", "3");
        strings.forEach(s->{
            System.out.println(s);
        });
    }


}
