package com.toyproject.bookmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Authority {
	private int authority_id;
	private int user_id;
	private int role_id;
	
	private Role role;
}
