package com.wang.project.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.wang.project.entity.UserModel;
import com.wang.project.entity.UserVO;
import com.wang.project.service.UserService;
import com.wang.project.utils.DozerBeanUtil;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(path="/getInfo/{account}",method=RequestMethod.GET)
	public UserVO getUserInfoByAccount(@PathVariable("account") String account) {
		UserModel userInfo = userService.getUserInfo(account);
		UserVO user = DozerBeanUtil.map(userInfo, UserVO.class);
		return user;
	}
	
	@RequestMapping(path="/getAllUsers",method=RequestMethod.GET)
	public List<UserVO> getAllUsers(){
		WebApplicationContext  applicationContext = WebApplicationContextUtils.findWebApplicationContext(request.getServletContext());
		WebApplicationContext context = RequestContextUtils.findWebApplicationContext(request);
		List<UserVO> userList = new ArrayList<>();
		List<UserModel> users = userService.getAllUsers();
		users.forEach(user -> {
			userList.add(DozerBeanUtil.map(user, UserVO.class));
		});
		return userList;
	}
}
  