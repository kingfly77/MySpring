package com.fly.spring.aop.advice;

import com.fly.spring.aop.AspectInstanceFactory;
import com.fly.spring.aop.pointcut.Pointcut;

import java.lang.reflect.Method;

public class BeforeAdvice extends AbstractAdvice {

    public BeforeAdvice(Method method, Pointcut pointcut, AspectInstanceFactory aif) {
        super(method, pointcut, aif);
    }

    public void before(Method method, Object[] args, Object target) {
        super.invokeAdviceMethod(method, args, target);
    }

}
