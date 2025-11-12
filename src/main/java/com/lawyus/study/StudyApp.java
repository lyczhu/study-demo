package com.lawyus.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudyApp {

    static void main(String[] args) {
		SpringApplication.run(StudyApp.class, args);
	}
}
