package com.tungstun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableOpenApi
public class BarApiApplication {
    public static void main(String[] args) {

//        Map<String, String> activeTravelers = new HashMap<>();
//        activeTravelers.keySet()
//                .stream()
//                .filter(mouse -> {
//                    if (true) return true;
////                    routes.add(entry.getValue());
//                    return false;
//                });
//
//    }

//	public static void main(String[] args) {
		SpringApplication.run(BarApiApplication.class, args);
	}
}
