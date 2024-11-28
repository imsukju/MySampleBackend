package com.practiceBackend.practiceBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
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


