package com.toyproject.bookmanagement.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtRespDto {
	private String grantType;
	private String accessToken;
}
