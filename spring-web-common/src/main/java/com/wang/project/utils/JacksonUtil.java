package com.wang.project.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonUtil {
	private static ObjectMapper mapper;

	static {
		getInstance();
	}

	private static void getInstance() {
		if (mapper == null) {
			mapper = new ObjectMapper();
		}
		mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.PUBLIC_ONLY);// 字段自动识别
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);// 是否环绕根元素
		mapper.configure(SerializationFeature.INDENT_OUTPUT, false);// 缩放输出
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd mm:HH:ss.SSS"));
	}

	/**
	 * 将java对象转换成json字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String beanToJson(Object object) {
		if (object instanceof String) {
			return (String) object;
		}
		if (mapper == null) {
			getInstance();
		}
		try {
			String value = mapper.writeValueAsString(object);
			return value;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 将Json转成对象
	 * 
	 * @param value
	 * @param clazz
	 * @return
	 */
	public static <T> T jsonToBean(String value, Class<T> clazz) {
		if (mapper == null) {
			getInstance();
		}
		try {
			T target = mapper.readValue(value, clazz);
			return target;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 将Json转成对象数组
	 * @param value
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> jsonToBeanList(String value, Class<T> clazz) {
		if (mapper == null) {
			getInstance();
		}
		JavaType valueType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
		try {
			List<T> list = mapper.readValue(value, valueType);
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
