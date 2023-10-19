package com.luidimso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.luidimso.data.vo.v1.security.AccountCredentialsVO;
import com.luidimso.data.vo.v1.security.TokenVO;
import com.luidimso.repositories.UserRepository;
import com.luidimso.security.jwt.JwtTokenProvider;

@Service
public class AuthService {
	
	@Autowired
	private AuthenticationManager autheticationManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	
	public ResponseEntity signin(AccountCredentialsVO account) {
		try {
			var username = account.getUsername();
			var password = account.getPassword();
			
			autheticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
			var user = userRepository.findByUserName(username);
			var tokenResponse = new TokenVO();
			
			if (user != null) {
				tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
			} else {
				throw new UsernameNotFoundException("Username not found: " + username);
			}
			
			return ResponseEntity.ok(tokenResponse);
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid username or password!"); 
		}
	}
}
