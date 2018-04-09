package com.wang.project.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6008428839436425581L;
	private String account;
	private String username;
	private String password;
}
