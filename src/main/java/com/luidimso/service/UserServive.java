package com.luidimso.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import com.luidimso.repositories.UserRepository;

@Service
public class UserServive implements UserDetailsService {
;
	private Logger logger = Logger.getLogger(UserServive.class.getName());
	
	@Autowired
	UserRepository repository;
	
	public UserServive(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Finding a user by name: " + username);
		
		var user = repository.findByUsername(username);
		
		if(user != null) {
			return user;
		} else {
			throw new UsernameNotFoundException("Username not found: "+username);
		}
	}


}
