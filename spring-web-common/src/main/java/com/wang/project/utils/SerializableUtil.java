package com.wang.project.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 
 * @author wyw
 * @data 2018年4月3日
 * @version 1.0
 */
public class SerializableUtil {
	/**
	 * 判断一个类是否可以序列化
	 * 
	 * @param clz
	 * @return
	 */
	public static synchronized boolean ableSerializeSynchronized(Class<?> clz) {
		// isAssignableFrom 是用来判断一个类和另一个类是否相同或是另一个类的超类或接口。
		boolean flag = false;
		flag = Serializable.class.isAssignableFrom(clz);
		return flag;
	}

	/**
	 * 判断一个类是否可以序列化
	 * 
	 * @param clz
	 * @return
	 */
	public static boolean ableSerialize(Class<?> clz) {
		// isAssignableFrom 是用来判断一个类和另一个类是否相同或是另一个类的超类或接口。
		boolean flag = false;
		flag = Serializable.class.isAssignableFrom(clz);
		return flag;
	}

	/**
	 * 判断该类是否是{@code Collection}类
	 * 
	 * @param clz
	 * @return
	 */
	public static boolean belongCollection(Class<?> clz) {
		// isAssignableFrom 是用来判断一个类和另一个类是否相同或是另一个类的超类或接口。
		boolean flag = false;
		flag = Collection.class.isAssignableFrom(clz);
		return flag;
	}

	/**
	 * 判断该类是否是{@code Map}类
	 * 
	 * @param clz
	 * @return
	 */
	public static boolean belongMap(Class<?> clz) {
		boolean flag = false;
		flag = Map.class.isAssignableFrom(clz);
		return flag;
	}

	/**
	 * 判断该对象是否是{@code Collection}类
	 * 
	 * @param clz
	 * @return
	 */
	public static boolean belongCollection(Object object) {
		if (object instanceof Collection) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断该对象是否是{@code Map}类
	 * 
	 * @param clz
	 * @return
	 */
	public static boolean belongMap(Object object) {
		if (object instanceof Map) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断对象是否是当前类的一个实例
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static boolean belongToTargetClass(Object object,Class<?> clazz) {
		if(object==null||clazz==null) {
			return false;
		}
		return clazz.isAssignableFrom(object.getClass());
	}
	
	/**
	 * 判断target是source的超类或接口,或者他们是相同的类
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean belongToTargetClass(Class<?> source,Class<?> target) {
		if(source==null||target==null) {
			return false;
		}
		return target.isAssignableFrom(source);
	}
}
