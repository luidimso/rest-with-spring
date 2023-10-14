package com.luidimso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luidimso.exceptions.ResourceNotFoundException;
import com.luidimso.interfaces.PersonRepository;
import com.luidimso.model.Person;

@Service
public class PersonService {
;
	private Logger logger = Logger.getLogger(PersonService.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	public Person findById(Long id) {
		logger.info("Finding a person");

		
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
	}
	
	public List<Person> findAll() {
		logger.info("Finding all people");

		
		return repository.findAll();
	}
	
	
	public Person create(Person person) {
		logger.info("Creating a person");
		return repository.save(person);
	}
	
	public Person update(Person person) {
		logger.info("Updating a person");
		
		var entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		return person;
	}

	
	public void delete(Long id) {
		logger.info("Deleting a person");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		repository.delete(entity);
	}

}
