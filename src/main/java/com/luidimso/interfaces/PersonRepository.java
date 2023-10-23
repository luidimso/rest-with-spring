package com.luidimso.interfaces;

import org.springframework.stereotype.Repository;

import com.luidimso.model.Person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
	
	
	// This annotation modifying is used for transactions that are modifying data
	@Modifying
	@Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
	void disablePerson(@Param("id") Long id);
	
	@Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT ('%',:firstName,'%'))")
	Page<Person> findPeopleByName(@Param("firstName") String firstName, Pageable pageable);
}
