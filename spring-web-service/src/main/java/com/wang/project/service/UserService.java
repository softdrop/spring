package com.wang.project.service;

import java.util.List;

import com.wang.project.entity.UserModel;

public interface UserService {
	public UserModel getUserInfo(String account);
	public List<UserModel> getAllUsers();
}
