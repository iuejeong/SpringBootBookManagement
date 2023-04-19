package com.toyproject.bookmanagement.dto.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.toyproject.bookmanagement.entity.User;

import lombok.Data;

@Data
public class SignupReqDto {
	
	@Email()
	private String email;
	// ^: 시작, $: 끝
	// (?=)를 하나로 본다. 앞 쪽에 있는 것을 일치하느냐로 본다.
	//  *은 모든 글자에 있거나 없거나(모든 글자가 여러 개 있거나 없거나), 필수는 +로 적는다.
	// [] 내용은 무조건 포함해야 함.
	// 첫 번째(): 모든 글자에서 A~z까지 포함되는지 확인.
	// 두 번째(): 모든 숫자를 의미. .*[0~9]와 똑같다.
	// 세 번째(): 모든 특수문자 포함.
	// 네 번째[]: [A-Za-z\\d@$!%*#?&] 포함되지 않으면 안 받겠다.
	// 마지막{8, }: 글자 개수(8자 이상. ,뒤에는 최댓값)
	// 이 5개가 모두 true가 돼야 함.
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$",
			message = "비밀번호는 영문자, 숫자, 특수문자를 포함하여 8 ~ 16자로 작성")	// validation 라이브러리가 있어야 함
	private String password;
	
	@Pattern(regexp = "^[가-힣]{2,7}$",
			message = "이름은 한글이름만 작성가능합니다.")
	private String name;
	
	public User toEntity() {
		return User.builder()
				.password(new BCryptPasswordEncoder().encode(password))
				.name(name)
				.email(email)
				.build();
	}
}
