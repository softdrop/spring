package com.wang.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wang.project.entity.User;

public interface UserMapper {
	public User selectOneByAccount(@Param("account") String account);
	public List<User> selectAll();
}
