package com.gocaspi.taskfly;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class for TaskflyApplication
 */
@SpringBootApplication
public class TaskflyApplication {
	/**
	 * Entry Point for TaskFlyApplication
	 * @param args String array
	 */
	public static void main(String[] args) {
		SpringApplication.run(TaskflyApplication.class, args);
	}

	@Value("${crossorigin.url}")
	private String frontendURL;
	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry){

				registry.addMapping("/**").allowedOrigins(frontendURL).allowedMethods("*").allowedHeaders("*");
			}
		};
	}

}
