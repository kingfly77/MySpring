package com.fly.spring.aop.advice;

import com.fly.spring.aop.AspectInstanceFactory;
import com.fly.spring.aop.pointcut.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AbstractAdvice implements Advice {


    private Method method;
    private Pointcut pointcut;
    private AspectInstanceFactory aif;

    public AbstractAdvice(Method method, Pointcut pointcut, AspectInstanceFactory aif) {
        this.method = method;
        this.pointcut = pointcut;
        this.aif = aif;
    }

    public Object invokeAdviceMethod(Method method, Object[] args, Object target) {
        try {
            return this.method.invoke(this.aif.getAspectInstance(), args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
