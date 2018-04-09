package com.wang.project.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.wang.project.utils.JacksonUtil;

import lombok.extern.log4j.Log4j2;

@Component
@Aspect
@Log4j2
public class MethodsAspect {

	@Pointcut("execution(* com.wang.project.controller.*.*(..))||execution(* com.wang.project.service.*.*(..))")
	public void markMethod() {

	}

	@Around(value = "markMethod()")
	public Object pointMethod(ProceedingJoinPoint pjp) throws Throwable {
		// 获取连接点所在的对象
		Object target = pjp.getTarget();
		// 获取对象签名
		String className = target.getClass().getName();
		// 获取方法签名
		String methodName = pjp.getSignature().getName();
		// 获取参数列表
		Object[] args = pjp.getArgs();
		StringBuffer argsTypes = new StringBuffer("(");
		StringBuffer argsValues = new StringBuffer("(");
		if (args != null && args.length > 0) {
			for (Object object : args) {
				// 获取参数的类型
				String argType = object.getClass().getName();
				argType.substring(argType.lastIndexOf(".") + 1);
				argsTypes.append(argType + ",");
				argsValues.append(JacksonUtil.beanToJson(object) + ",");
			}
		}
		argsTypes = new StringBuffer(
				argsTypes.substring(0, argsTypes.length() > 1 ? (argsTypes.length() - 1) : argsTypes.length()) + ")");
		argsValues = new StringBuffer(
				argsValues.subSequence(0, argsValues.length() > 1 ? (argsValues.length() - 1) : argsValues.length())
						+ ")");
		String description = className.substring(className.lastIndexOf(".") + 1) + "." + methodName + argsTypes;
		log.debug(String.format(">>>>>>>>>>>>>>>>>>>>>>Start %s", description));
		log.debug(String.format(">>>>>>>>>>>>>>>>>>>>>>args is %s", argsValues + "<<<<<<<<<<<<<<<<<<<<<<"));
		long startTime = System.currentTimeMillis();
		Object object = pjp.proceed();
		long endTime = System.currentTimeMillis();
		long costTime = endTime - startTime;
		float costTotalSeconds = costTime / 1000f;
		log.debug(String.format(">>>>>>>>>>>>>>>>>>>>>>End %s", description));
		log.debug(String.format(">>>>>>>>>>>>>>>>>>>>>>return is (%s",
				JacksonUtil.beanToJson(object) + ")<<<<<<<<<<<<<<<<<<<<<<"));
		if (costTime > 5000) {
			log.debug("!!!!!!!!!!!!!!!!       the method " + description + "cost to much time: " + costTotalSeconds
					+ " seconds       !!!!!!!!!!!!!!!!");
		} else {
			log.debug("=================       the method " + description + " cost time is " + costTotalSeconds
					+ " seconds       =================");
		}
		return object;
	}
}
