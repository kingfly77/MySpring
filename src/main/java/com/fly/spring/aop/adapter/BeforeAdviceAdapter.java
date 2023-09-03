package com.fly.spring.aop.adapter;

import com.fly.spring.aop.advisor.Advisor;
import com.fly.spring.aop.advice.Advice;
import com.fly.spring.aop.advice.BeforeAdvice;
import com.fly.spring.aop.interceptor.BeforeAdviceInterceptor;
import com.fly.spring.aop.interceptor.Interceptor;

public class BeforeAdviceAdapter implements AdvisorAdapter {
    @Override
    public boolean supportsAdvice(Advice advice) {
        return (advice instanceof BeforeAdvice);
    }

    @Override
    public Interceptor getInterceptor(Advisor advisor) {
        BeforeAdvice advice = (BeforeAdvice) advisor.getAdvice();
        return new BeforeAdviceInterceptor(advice);
    }
}
