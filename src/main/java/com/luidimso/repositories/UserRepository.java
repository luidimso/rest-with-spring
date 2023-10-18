package com.luidimso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luidimso.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>{
	@Query("SELECT u FROM Users WHERE u.userName =:userName")
	Users findByUserName(@Param("userName") String userName);
}
