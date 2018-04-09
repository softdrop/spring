package com.wang.project.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7586556107279452845L;
	private String account;
	private String username;
	private String password;
}
