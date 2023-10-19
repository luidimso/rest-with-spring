package com.luidimso.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.luidimso.data.vo.v1.security.TokenVO;
import com.luidimso.exceptions.InvalidJwtAuthenticationException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

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
		var refreshToken = getRefreshToken(username, roles, now);
		return new TokenVO(username, true, now, v, accessToken, refreshToken);
	}

	private String getRefreshToken(String username, List<String> roles, Date now) {
		Date validityRefreshToken = new Date(now.getTime() + (validity * 3));
		return JWT.create().withClaim("roles", roles).withIssuedAt(now).withExpiresAt(validityRefreshToken).withSubject(username).sign(algotithm).strip();
	}

	private String getAccessToken(String username, List<String> roles, Date now, Date v) {
		var issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
		return JWT.create().withClaim("roles", roles).withIssuedAt(now).withExpiresAt(v).withSubject(username).withIssuer(issuerUrl).sign(algotithm).strip();
	}
	
	public Authentication getAuthentication(String token) {
		DecodedJWT decodeJwt = decodedToken(token);
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodeJwt.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private DecodedJWT decodedToken(String token) {
		Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
		JWTVerifier verifier = JWT.require(alg).build();
		DecodedJWT decodedJwt = verifier.verify(token);
		return decodedJwt;
	}
	
	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		
		if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring("Bearer ".length());
		}
		
		return null;
	}
	
	public boolean validateToken(String token) {
		DecodedJWT decodedJwt = decodedToken(token);
		
		try {
			if(decodedJwt.getExpiresAt().before(new Date())) {
				return false;
			}
			
			return true;
		} catch (Exception e) {
			throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
		}
	}
}

