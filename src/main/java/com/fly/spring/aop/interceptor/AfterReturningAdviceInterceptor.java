package com.fly.spring.aop.interceptor;

import com.fly.spring.aop.invocation.Invocation;
import com.fly.spring.aop.advice.AfterReturningAdvice;

public class AfterReturningAdviceInterceptor implements Interceptor {


    private final AfterReturningAdvice advice;

    public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        Object retVal = invocation.proceed();
        this.advice.afterReturning(retVal, invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return retVal;
    }

}
