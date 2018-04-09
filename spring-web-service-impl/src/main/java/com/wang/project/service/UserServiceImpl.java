package com.wang.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wang.project.entity.User;
import com.wang.project.entity.UserModel;
import com.wang.project.mapper.UserMapper;
import com.wang.project.utils.DozerBeanUtil;
import com.wang.project.utils.JacksonUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper usermapper;

	@Override
	public UserModel getUserInfo(String account) {
		User user = usermapper.selectOneByAccount(account);
		log.debug("查询到的数据是："+JacksonUtil.beanToJson(user));
		UserModel userInfo = DozerBeanUtil.map(user, UserModel.class);
		return userInfo;
	}

	@Override
	public List<UserModel> getAllUsers() {
		List<User> userList = usermapper.selectAll();
		log.debug("查询到的数据是："+JacksonUtil.beanToJson(userList));
		List<UserModel> users = new ArrayList<>();
		userList.forEach(user -> {
			users.add(DozerBeanUtil.map(user, UserModel.class));
		});
		return users;
	}

}
