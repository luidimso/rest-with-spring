package com.luidimso.securityJwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;
import com.luidimso.data.vo.v1.security.TokenVO;

import jakarta.annotation.PostConstruct;

@Service
public class JwtTokenProvider {
	@Value("${security.jwt:.token.secret-key:secret}")
	private String secretKey = "secret";
	
	@Value("${security.jwt:.token.expire-length:3600000}")
	private long validity = 3600000;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	Algorithm algotithm = null;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		algotithm = Algorithm.HMAC256(secretKey.getBytes());
	}
	
	public TokenVO createAccessToken(String username, List<String> roles) {
		Date now = new  Date();
		Date v = new Date(now.getTime() + validity);
		var accessToken = getAccessToken(username, roles, now, v);
		var refreshToken = getAccessToken(username, roles, now);
		return new TokenVO(username, true, now, v, accessToken, refreshToken);
	}

	private String getAccessToken(String username, List<String> roles, Date now) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getAccessToken(String username, List<String> roles, Date now, Date v) {
		// TODO Auto-generated method stub
		return null;
	}
}
