package com.toyproject.bookmanagement.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toyproject.bookmanagement.aop.annotation.ValidAspect;
import com.toyproject.bookmanagement.dto.auth.LoginReqDto;
import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")	// 이 컨트롤러 안에 들어오는 Mapping은 앞에 다 /auth가 붙는다.
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	
	@ValidAspect
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginReqDto loginReqDto, BindingResult bindingResult) {
		
		// 값을 ok 안에 넣는 거나 body 안에 넣는 거나 똑같다. 하지만 badRequest는 안 됨. ok만 가능
		return ResponseEntity.ok(authenticationService.signin(loginReqDto));
	}
	
	@ValidAspect
	@PostMapping("/signup")
	// @Valid를 달아주면 Dto 안에 있는 email(), Pattern()을 검사해준다
	// @Valid와 BindingResult는 세트임. signReqDto의 오류를 BindingResult에게 모두 넘겨준다.
	public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult ) {
		authenticationService.checkDuplicatedEmail(signupReqDto.getEmail());
		authenticationService.signup(signupReqDto);
		return ResponseEntity.ok().body(true);
	}
	
	@GetMapping("/authenticated")
	public ResponseEntity<?> authenticated(String accessToken) {
		
		return ResponseEntity.ok().body(authenticationService.authenticated(accessToken));
	}
	
	@GetMapping("/principal")
	public ResponseEntity<?> principal(String accessToken) {
		System.out.println(accessToken);
		return ResponseEntity.ok().body(authenticationService.getPrincipal(accessToken));
	}
	
}

















