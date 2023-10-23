package com.luidimso.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.luidimso.PersonController;
import com.luidimso.data.vo.v1.PersonVO;
import com.luidimso.data.vo.v2.PersonVOV2;
import com.luidimso.exceptions.RequiredObjectIsNullException;
import com.luidimso.exceptions.ResourceNotFoundException;
import com.luidimso.interfaces.PersonRepository;
import com.luidimso.mapper.DozerMapper;
import com.luidimso.mapper.custom.PersonMapper;
import com.luidimso.model.Person;

import jakarta.transaction.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Service
public class PersonService {
;
	private Logger logger = Logger.getLogger(PersonService.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	@Autowired
	PersonMapper mapper;
	
	@Autowired
	PagedResourcesAssembler<PersonVO> assembler;
	
	public PersonVO findById(Long id) {
		logger.info("Finding a person");

		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		PersonVO personVo = DozerMapper.parseObjact(entity, PersonVO.class);
		
		try {
			personVo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return personVo;
	}
	
	public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) throws Exception {
		logger.info("Finding all people");
		
		var peoplePage = repository.findAll(pageable);
		var peopleVOPage = peoplePage.map(p -> DozerMapper.parseObjact(p, PersonVO.class));
		peopleVOPage.map(p -> {
			try {
				return p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return p;
		});

		
		Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		
		return assembler.toModel(peopleVOPage, link);
	}
	
	
	public PagedModel<EntityModel<PersonVO>> findPeopleByName(String firstName, Pageable pageable) throws Exception {
		logger.info("Finding all people");
		
		var peoplePage = repository.findPeopleByName(firstName, pageable);
		var peopleVOPage = peoplePage.map(p -> DozerMapper.parseObjact(p, PersonVO.class));
		peopleVOPage.map(p -> {
			try {
				return p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return p;
		});

		
		Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		
		return assembler.toModel(peopleVOPage, link);
	}
	
	
	public PersonVO create(PersonVO person) {
		logger.info("Creating a person");
		
		if(person == null) {
			throw new RequiredObjectIsNullException();
		}
		
		var entity = DozerMapper.parseObjact(person, Person.class);
		var entityVo =  DozerMapper.parseObjact(repository.save(entity), PersonVO.class);
		
		return entityVo;
	}
	
	public PersonVOV2 createV2(PersonVOV2 person) {
		logger.info("Creating a person on version 2");
		
		if(person == null) {
			throw new RequiredObjectIsNullException();
		}
		
		var entity = mapper.convertVOToEntity(person);
		var entityVo =  mapper.convertEntityToVo(repository.save(entity));
		
		return entityVo;
	}
	
	public PersonVO update(PersonVO person) {
		logger.info("Updating a person");
		
		if(person == null) {
			throw new RequiredObjectIsNullException();
		}
		
		var entity = repository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		var entityVo =  DozerMapper.parseObjact(repository.save(entity), PersonVO.class);
		
		return entityVo;
	}

	
	public void delete(Long id) {
		logger.info("Deleting a person");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		repository.delete(entity);
	}
	
	
	@Transactional
	
	public PersonVO disablePerson(Long id) {
		
		logger.info("Disabling a person");
		
		repository.disablePerson(id);
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		PersonVO personVo = DozerMapper.parseObjact(entity, PersonVO.class);
		
		try {
			personVo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return personVo;
	}

}
