package com.practiceBackend.practiceBackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "My API", version = "1.0", description = "My API Documentation"))
public class PracticeBackendApplication {


	public static void main(String[] args) {
//		SpringApplication.run(PracticeBackendApplication.class, args);

		ConfigurableApplicationContext context = SpringApplication.run(PracticeBackendApplication.class, args);
		// 등록된 빈 이름 출력
		String[] beanNames = context.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
	}
}


