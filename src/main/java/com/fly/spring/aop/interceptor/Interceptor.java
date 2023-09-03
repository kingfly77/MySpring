package com.fly.spring.aop.interceptor;

import com.fly.spring.aop.invocation.Invocation;
import com.fly.spring.aop.advice.Advice;

public interface Interceptor extends Advice {

    Object invoke(Invocation invocation) throws Throwable;

}
