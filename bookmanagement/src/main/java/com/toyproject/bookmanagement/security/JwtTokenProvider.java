package com.toyproject.bookmanagement.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;
import com.toyproject.bookmanagement.exception.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
			
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported Jwt Token", e);
		} catch (IllegalArgumentException e) {
			log.info("IllegalArgument Jwt Token", e);
		} catch (Exception e) {
			log.info("Jwt Token Error", e);
		}
		
		return false;
	}
	
	public String getToken(String token) {
		String type = "Bearer";
		if(StringUtils.hasText(token) && token.startsWith(type)) {
			return token.substring(type.length() + 1);
		}
		
		return null;
		
	}
	
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public Authentication getAuthentication(String accessToken) {
		Authentication authentication = null;
		
		Claims claims = getClaims(accessToken);
		if(claims.get("auth") == null) {
			throw new CustomException("AccessToken에 권한 정보가 없습니다.");
		}
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		String auth = claims.get("auth").toString();		// 권한이 없으면 null이라고 문자열로 나온다.
		for(String role: auth.split(",")) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		
		UserDetails userDetails = new User(claims.getSubject(), "", authorities);		// password는 토큰이 없기 때문에 빈값을 줌
		
		authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
		return authentication;
	}
	
}
















