package com.luidimso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {
	// http://localhost:8080/v3/api-docs
	// http://localhost:8080/swagger-ui/index.html
	
	
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("REST with Springboot").version("v1").description("Some description").termsOfService("Some link"));
	}
	
}
