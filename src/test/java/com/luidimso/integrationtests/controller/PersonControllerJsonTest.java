package com.luidimso.integrationtests.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonParseException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.luidimo.integrationtests.vo.PersonVO;
import com.luidimso.configs.TestConfigs;
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
	@Order(1)
	public void testCreate() throws JsonParseException, JsonMappingException, IOException {
		mockPerson();
		
		specification = new RequestSpecBuilder()
							.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
							.setBasePath("/api/person/v1")
							.setPort(port)
							.addFilter(new RequestLoggingFilter(LogDetail.ALL))
							.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
							.build();
		
		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(person)
				.port(port)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();
		
		PersonVO createPerson = objectMapper.readValue(content, PersonVO.class);
		
		assertNotNull(createPerson);
		assertTrue(createPerson.getId() > 0);
		assertEquals("John", createPerson.getFirstName());
		assertEquals("Cena", createPerson.getLastName());
		assertEquals("United States", createPerson.getAddress());
		assertEquals("Male", createPerson.getGender());
	}

	private void mockPerson() {
		person.setFirstName("John");
		person.setLastName("Cena");
		person.setAddress("United States");
		person.setGender("Male");
	}

}
