package com.wang.project.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8323358535376582774L;
	private String account;
	private String username;
	private String password;
}
