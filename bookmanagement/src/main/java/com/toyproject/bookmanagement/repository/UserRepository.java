package com.toyproject.bookmanagement.repository;

import org.apache.ibatis.annotations.Mapper;

import com.toyproject.bookmanagement.entity.Authority;
import com.toyproject.bookmanagement.entity.User;

@Mapper
public interface UserRepository {
	public int saveUser(User user);
	public int saveAuthority(Authority authorities);
	public User findUserByEmail(String email);
}
