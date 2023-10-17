package com.luidimso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${cors.originPatterns:default}")
	private String corsoriginPatterns = "";

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		String allowedOrigins = "http://localhost:8080,https://luidimso.com";
		
		registry.addMapping("/**")
			.allowedMethods("POST")
			.allowedOriginPatterns(allowedOrigins)
			.allowCredentials(true);
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		// Via query params, for configure the service for make the response with a extension, on this example is XML
		// ebMvcConfigurer.super.configureContentNegotiation(configurer.favorParameter(true).parameterName("mediaType").ignoreAcceptHeader(true).useRegisteredExtensionsOnly(false).defaultContentType(MediaType.APPLICATION_JSON).mediaType("json", MediaType.APPLICATION_JSON).mediaType("xml", MediaType.APPLICATION_XML));
	
		WebMvcConfigurer.super.configureContentNegotiation(configurer.favorParameter(true).ignoreAcceptHeader(false).useRegisteredExtensionsOnly(false).defaultContentType(MediaType.APPLICATION_JSON).mediaType("json", MediaType.APPLICATION_JSON).mediaType("xml", MediaType.APPLICATION_XML));
	}

}
