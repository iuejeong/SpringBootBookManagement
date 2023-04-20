package com.toyproject.bookmanagement.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtTokenProvider {
	
	private final Key key;
	
	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
	
	public JwtRespDto generateToken(Authentication authentication) {
		StringBuilder builder = new StringBuilder();
		
		authentication.getAuthorities().forEach(grantedAuthority -> {
			builder.append(grantedAuthority.getAuthority() + ",");
		});
		
		builder.delete(builder.length() - 1, builder.length());
		
		String authorities = builder.toString();
		
		Date tokenExpiresDate = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));	// 현재 시간 + 하루
		
		PrincipalUser userDetails = (PrincipalUser) authentication.getPrincipal();
		
		String accessToken = Jwts.builder()
				.setSubject(authentication.getName())
				.claim("auth", authorities)
				.setExpiration(tokenExpiresDate)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
		
		return JwtRespDto.builder()
				.grantType("Bearer")
				.accessToken(accessToken)
				.build();
	}
	
	
	
}
