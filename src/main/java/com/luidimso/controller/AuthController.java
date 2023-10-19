package com.luidimso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luidimso.data.vo.v1.security.AccountCredentialsVO;
import com.luidimso.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication Endpoint")
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthService authService;
	
	@SuppressWarnings("rawtypes")
	@Operation(summary = "Authenticates a user and returns a token")
	@PostMapping(value = "/singin")
	public ResponseEntity sign(@RequestBody AccountCredentialsVO account) {
		if(checkParams(account)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid account details!");
		}
		
		var token = authService.signin(account);
		
		if(token == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid account details!");
		}
		
		return token;
	}

	private boolean checkParams(AccountCredentialsVO account) {
		return (account == null || account.getUsername() == null || account.getUsername().isBlank() || account.getPassword() == null || account.getPassword().isBlank());
	}
}
