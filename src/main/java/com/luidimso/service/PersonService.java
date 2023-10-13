package com.luidimso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.luidimso.model.Person;

@Service
public class PersonService {

	private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(PersonService.class.getName());
	
	public Person findById(String id) {
		logger.info("Finding a person");
		
		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Luidi");
		person.setLastName("Matheus");
		person.setAddress("Brazil");
		person.setGender("Male");
		
		return person;
	}
	
	public List<Person> findAll() {
		logger.info("Finding all people");
	
		List<Person> persons = new ArrayList<>();
		
		for(int i = 0; i < 8; i++) {
			Person person = mockPerson();
			persons.add(person);
		}
		
		return persons;
	}
	
	public Person mockPerson() {
		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Luidi");
		person.setLastName("Matheus");
		person.setAddress("Brazil");
		person.setGender("Male");
		
		return person;
	}
	
	
	public Person create(Person person) {
		logger.info("Creating a person");
		return person;
	}
	
	public Person update(Person person) {
		logger.info("Updating a person");
		return person;
	}

	
	public void delete(String id) {
		logger.info("Deleting a person");
	}

}
