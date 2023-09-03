package com.fly.spring.aop.adapter;

import com.fly.spring.aop.advice.Advice;
import com.fly.spring.aop.advisor.Advisor;
import com.fly.spring.aop.interceptor.Interceptor;

public interface AdvisorAdapter {

    boolean supportsAdvice(Advice advice);

    Interceptor getInterceptor(Advisor advisor);

}
