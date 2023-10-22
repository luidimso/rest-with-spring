package com.luidimso.interfaces;

import org.springframework.stereotype.Repository;

import com.luidimso.model.Person;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
	
	
	// This annotation modifying is used for transactions that are modifying data
	@Modifying
	@Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
	void disablePerson(@Param("id") Long id);
}
