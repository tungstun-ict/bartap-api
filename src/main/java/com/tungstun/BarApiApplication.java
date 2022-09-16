package com.tungstun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.tungstun")
@SpringBootApplication
public class BarApiApplication {
    public static void main(String[] args) {
		SpringApplication.run(BarApiApplication.class, args);
	}
}
