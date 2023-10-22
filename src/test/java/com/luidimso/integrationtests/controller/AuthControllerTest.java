package com.luidimso.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import com.luidimso.configs.TestConfigs;
import com.luidimso.data.vo.v1.security.AccountCredentialsVO;
import com.luidimso.data.vo.v1.security.TokenVO;
import com.luidimso.integrationtests.testescontainers.AbstractIntegrationTest;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest extends AbstractIntegrationTest {
	private static TokenVO tokenVO;
	
	@LocalServerPort
	private int port;
	
	@Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		tokenVO = given()
						.basePath("/auth/signin")
						.port(port)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.body(user)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(TokenVO.class);
		
		 assertNotNull(tokenVO.getAccessToken());
		 assertNotNull(tokenVO.getRefreshToken());
		 assertTrue(tokenVO.getAccessToken().length() > 10);
		 assertTrue(tokenVO.getRefreshToken().length() > 10);
	}
	
	
	@Test
	@Order(2)
	public void testRefreshToken() throws JsonMappingException, JsonProcessingException {
		var newtokenVO = given()
						.basePath("/auth/refresh")
						.port(port)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.pathParam("username", tokenVO.getUsername())
						.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
						.when()
						.put("{username}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(TokenVO.class);
		
		 assertNotNull(newtokenVO.getAccessToken());
		 assertNotNull(newtokenVO.getRefreshToken());
		 assertTrue(newtokenVO.getAccessToken().length() > 10);
		 assertTrue(newtokenVO.getRefreshToken().length() > 10);
	}
}
