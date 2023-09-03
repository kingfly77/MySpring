package com.fly.spring.aop.adapter;

import com.fly.spring.aop.advisor.Advisor;
import com.fly.spring.aop.advice.Advice;
import com.fly.spring.aop.advice.AfterReturningAdvice;
import com.fly.spring.aop.interceptor.AfterReturningAdviceInterceptor;
import com.fly.spring.aop.interceptor.Interceptor;

public class AfterReturningAdviceAdapter implements AdvisorAdapter{

    @Override
    public boolean supportsAdvice(Advice advice) {
        return (advice instanceof AfterReturningAdvice);
    }

    @Override
    public Interceptor getInterceptor(Advisor advisor) {
        AfterReturningAdvice advice = (AfterReturningAdvice) advisor.getAdvice();
        return new AfterReturningAdviceInterceptor(advice);
    }
}
