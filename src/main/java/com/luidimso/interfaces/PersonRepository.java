package com.luidimso.interfaces;

import org.springframework.stereotype.Repository;

import com.luidimso.model.Person;

import org.springframework.data.jpa.repository.*;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{

}
