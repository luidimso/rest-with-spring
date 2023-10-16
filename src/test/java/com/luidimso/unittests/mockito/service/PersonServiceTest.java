package com.luidimso.unittests.mockito.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.luidimso.data.vo.v1.PersonVO;
import com.luidimso.interfaces.PersonRepository;
import com.luidimso.model.Person;
import com.luidimso.service.PersonService;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
	
	MockPerson input;
	
	@InjectMocks
	private PersonService service;
	
	@Mock
	PersonRepository repository;

	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPerson();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Person person = input.mockEntity(0);
		person.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(person));
		
		var result = service.findById(1L);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test0", result.getAddress());
		assertEquals("First Name Test0", result.getFirstName());
		assertEquals("Male", result.getGender());	
	}	
	
//	@Test
//	void testFindAll() {
//		fail("Not yet implemented");
//	}
//
	@Test
	void testCreate() {
		// this test makes what the service.save does, it mocks the personVo that it receives as parameter and mocks what repository,save returns, and it should only test the return
	
		Person entity = input.mockEntity(0);
		
		Person persisted = entity;
		persisted.setId(1L);
		
		PersonVO vo = input.mockVO();
		vo.setKey(1L);
		
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.create(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		
		assertEquals("Addres Test0", result.getAddress());
		assertEquals("First Name Test0", result.getFirstName());
		assertEquals("Male", result.getGender());	
		
	}
//
//	@Test
//	void testCreateV2() {
//		fail("Not yet implemented");
//	}
//
	@Test
	void testUpdate() {
		Person entity = input.mockEntity(0);
		entity.setId(1L);
		
		Person persisted = entity;
		persisted.setId(1L);
		
		PersonVO vo = input.mockVO();
		vo.setKey(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.update(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		
		assertEquals("Addres Test0", result.getAddress());
		assertEquals("First Name Test0", result.getFirstName());
		assertEquals("Male", result.getGender());	
		
	}
//
	@Test
	void testDelete() {
		Person entity = input.mockEntity(0);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		service.delete(1L);
	}

}
