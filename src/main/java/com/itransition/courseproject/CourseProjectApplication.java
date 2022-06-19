package com.itransition.courseproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication
public class CourseProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseProjectApplication.class, args);
    }

}
