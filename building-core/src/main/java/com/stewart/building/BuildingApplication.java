package com.stewart.building;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.stewart.building.mbg.mapper")
public class BuildingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuildingApplication.class, args);
    }

}
