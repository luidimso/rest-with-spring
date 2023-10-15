package com.luidimso.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.luidimso.data.vo.v2.PersonVOV2;
import com.luidimso.model.Person;

@Service
public class PersonMapper {
	public PersonVOV2 convertEntityToVo(Person person) {
		PersonVOV2 entityVo = new PersonVOV2();
		
		entityVo.setAddress(person.getAddress());
		entityVo.setBirthDay(new Date());
		entityVo.setFirstName(person.getFirstName());
		entityVo.setGender(person.getGender());
		entityVo.setId(person.getId());
		entityVo.setLastName(person.getLastName());
		
		return null;
	}
	
	
	public Person convertVOToEntity(PersonVOV2 person) {
		PersonVOV2 entity = new PersonVOV2();
		
		entity.setAddress(person.getAddress());
		entity.setBirthDay(new Date());
		entity.setFirstName(person.getFirstName());
		entity.setGender(person.getGender());
		entity.setId(person.getId());
		entity.setLastName(person.getLastName());
		
		return null;
	}
}
