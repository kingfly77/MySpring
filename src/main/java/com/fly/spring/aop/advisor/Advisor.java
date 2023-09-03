package com.fly.spring.aop.advisor;

import com.fly.spring.aop.advice.Advice;
import com.fly.spring.aop.pointcut.Pointcut;

public interface Advisor {

    Pointcut getPointcut();

    Advice getAdvice();
}
