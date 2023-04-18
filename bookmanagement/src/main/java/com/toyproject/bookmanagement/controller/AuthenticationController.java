package com.toyproject.bookmanagement.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toyproject.bookmanagement.aop.annotation.ValidAspect;
import com.toyproject.bookmanagement.dto.auth.SignupReqDto;

@RestController
@RequestMapping("/auth")	// 이 컨트롤러 안에 들어오는 Mapping은 앞에 다 /auth가 붙는다.
public class AuthenticationController {

	@PostMapping("/login")
	public ResponseEntity<?> login() {
		return ResponseEntity.ok(null);
	}
	
	@CrossOrigin
	@ValidAspect
	@PostMapping("/signup")
	// @Valid를 달아주면 Dto 안에 있는 email(), Pattern()과 BindingResult를 검사해준다
	public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult ) {
		return ResponseEntity.ok(null);
	}
}
