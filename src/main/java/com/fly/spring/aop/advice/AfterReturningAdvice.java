package com.fly.spring.aop.advice;

import com.fly.spring.aop.AspectInstanceFactory;
import com.fly.spring.aop.pointcut.Pointcut;

import java.lang.reflect.Method;

public class AfterReturningAdvice extends AbstractAdvice {

    public AfterReturningAdvice(Method method, Pointcut pointcut, AspectInstanceFactory aif) {
        super(method, pointcut, aif);
    }

    public void afterReturning(Object retVal, Method method, Object[] args, Object target) {
        // TODO: no use for retVal
        super.invokeAdviceMethod(method, args, target);
    }
}
