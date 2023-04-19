package com.toyproject.bookmanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.entity.Authority;
import com.toyproject.bookmanagement.entity.User;
import com.toyproject.bookmanagement.exception.CustomException;
import com.toyproject.bookmanagement.exception.ErrorMap;
import com.toyproject.bookmanagement.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final UserRepository userRepository;
	
	public void signup(SignupReqDto signupReqDto) {
		User userEntity = signupReqDto.toEntity();
		userRepository.saveUser(userEntity);	// insert user_mst
		
		List<Authority> authorities = new ArrayList<>();
		authorities.add(
				Authority.builder().
				user_id(userEntity.getUserId()).
				role_id(1).
				build());
		
		userRepository.addAuthorities(authorities);		// role도 등록
	}
	
	public void checkDuplicatedEmail(String email) {
		if(userRepository.findUserByEmail(email) != null) {
			throw new CustomException("Duplicated Email", 
					ErrorMap.builder()
					.put("email", "사용 중인 email입니다.")
					.build());
		}
	}
}
