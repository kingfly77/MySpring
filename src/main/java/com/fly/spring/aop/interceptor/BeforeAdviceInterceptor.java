package com.fly.spring.aop.interceptor;

import com.fly.spring.aop.invocation.Invocation;
import com.fly.spring.aop.advice.BeforeAdvice;

public class BeforeAdviceInterceptor implements Interceptor {

    private final BeforeAdvice advice;

    public BeforeAdviceInterceptor(BeforeAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        this.advice.before(invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return invocation.proceed();
    }
}
