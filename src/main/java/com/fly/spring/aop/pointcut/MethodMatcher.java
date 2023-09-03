package com.fly.spring.aop.pointcut;

import java.lang.reflect.Method;

public interface MethodMatcher {

    boolean matches(Method method, Class<?> targetClass) throws ClassNotFoundException;

}
