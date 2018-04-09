package com.wang.project.utils;


/**
 * @author wyw
 * @data 2018年4月2日
 * @version 1.0
 */
public class StringUtil {

	/**
	 * 
	 * @param str
	 * @return
	 */
	public synchronized static boolean isNotBlankSynchronized(String str) {
		if (null == str) {
			return false;
		}
		if ("".equals(str.trim())) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		if (null == str) {
			return false;
		}
		if ("".equals(str.trim())) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return !isNotBlank(str);
	}
}
