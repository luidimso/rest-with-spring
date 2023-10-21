package com.luidimso.integrationtests.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonParseException;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.luidimo.integrationtests.vo.PersonVO;
import com.luidimso.configs.TestConfigs;
import com.luidimso.data.vo.v1.security.AccountCredentialsVO;
import com.luidimso.data.vo.v1.security.TokenVO;
import com.luidimso.integrationtests.testescontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest()
public class PersonControllerJsonTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static PersonVO person;
	
	@LocalServerPort
	private int port;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		person = new PersonVO();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		var accessToken = given()
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
						.as(TokenVO.class)
						.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer "+accessToken)
				.setBasePath("/api/person/v1")
				.setPort(port)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonParseException, JsonMappingException, IOException {
		mockPerson();
		
		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.RIGHT_ORIGIN)
				.body(person)
				.port(port)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();
		
		person = objectMapper.readValue(content, PersonVO.class);
		
		assertNotNull(person);
		assertTrue(person.getId() > 0);
		assertEquals("John", person.getFirstName());
		assertEquals("Cena", person.getLastName());
		assertEquals("United States", person.getAddress());
		assertEquals("Male", person.getGender());
	}
	
	@Test
	@Order(2)
	public void testCreateWithWrongOrigin() throws JsonParseException, JsonMappingException, IOException {
		mockPerson();
		
		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.WRONG_ORIGIN)
				.body(person)
				.port(port)
				.when()
				.post()
				.then()
				.statusCode(403)
				.extract()
				.body()
				.asString();
		
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}
	
	
//	@Test
//	@Order(2)
//	public void testGetAPerson() throws JsonParseException, JsonMappingException, IOException {
//		mockPerson();
//		
//		specification = new RequestSpecBuilder()
//							.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.RIGHT_ORIGIN)
//							.setBasePath("/api/person/v1")
//							.setPort(port)
//							.addFilter(new RequestLoggingFilter(LogDetail.ALL))
//							.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
//							.build();
//		
//		var content = given()
//				.spec(specification)
//				.contentType(TestConfigs.CONTENT_TYPE_JSON)
//				.pathParam("id", person.getId())
//				.port(port)
//				.when()
//				.get("{id}")
//				.then()
//				.statusCode(200)
//				.extract()
//				.body()
//				.asString();
//		
//		PersonVO returnedPerson = objectMapper.readValue(content, PersonVO.class);
//		
//		assertNotNull(returnedPerson);
//		assertTrue(returnedPerson.getId() > 0);
//		assertEquals(returnedPerson.getFirstName(), person.getFirstName());
//		assertEquals(returnedPerson.getLastName(), person.getLastName());
//		assertEquals(returnedPerson.getAddress(), person.getAddress());
//		assertEquals(returnedPerson.getGender(), person.getGender());
//	}

	private void mockPerson() {
		person.setFirstName("John");
		person.setLastName("Cena");
		person.setAddress("United States");
		person.setGender("Male");
	}

}
