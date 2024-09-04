package com.errand;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.errand.mapper")
public class ErrandApplication {

    public static void main(String[] args) {

        SpringApplication.run(ErrandApplication.class);

    }
}
