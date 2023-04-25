package com.toyproject.bookmanagement.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.toyproject.bookmanagement.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String accessToken = httpRequest.getHeader("Authorization");		// 기존 토큰을 가져옴
		accessToken = jwtTokenProvider.getToken(accessToken);	// 가져온 것은 subString
		boolean validationFlag = jwtTokenProvider.validateToken(accessToken);	// subString 이후 유효성 검사 진행(예외가 일어나고 true, false 검사)
		
		//	false일 경우, Authentication 객체를 생성하지 못했기 때문에 403 error가 뜰 것임
		if(validationFlag) {
			Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
			// 이 안에 등록이 되어야지만 로그인이 된 것
			// true로 바꿔주는 과정
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		chain.doFilter(request, response);
		
	}

}
