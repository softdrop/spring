package com.wang.project.utils;


import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;


/**
 * Created by keyaming on 2017/7/25.
 */
public class DozerBeanUtil {

    private DozerBeanUtil() {
    }

    private final static Mapper mapper = new DozerBeanMapper();

    private static Mapper getDozerBeanMapper() {
        return mapper;
    }

    /**
     * 基于Dozer转换对象的类型.
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        return Optional.ofNullable(source).map(s -> getDozerBeanMapper().map(source, destinationClass)).orElse(null);
    }

    /**
     * 基于Dozer转换对象的类型.
     */
    public static void map(Object source, Object destination) {
        getDozerBeanMapper().map(source, destination);
    }
}
