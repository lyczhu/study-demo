package com.lawyus.study;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, RedisAutoConfiguration.class})
//@EnableBatchProcessing(modular = true)
@RestController
public class BootDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootDemoApplication.class, args);
	}

	@GetMapping("/test/{id}")
	public String test(@PathVariable Integer id) {
        for (int i = 0; i < 8; i++) {
            if (i == 7)
                continue;
            System.out.println();
        }
		return "success";
	}
}
