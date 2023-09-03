package com.fly.spring.test;

import com.fly.spring.annotations.Component;
import com.fly.spring.aop.annotations.AfterReturning;
import com.fly.spring.aop.annotations.Aspect;
import com.fly.spring.aop.annotations.Before;
import com.fly.spring.aop.annotations.Pointcut;

@Aspect
@Component
public class MyAspect {

    @Pointcut("@annotation(com.fly.spring.test.MyPointcut)")
    public void pointcut() { }

    @Before("pointcut()")
    public Object before() {
        System.out.println("----------------  before -----------------------");
        return null;
    }

    @AfterReturning("pointcut()")
    public Object afterReturning() {
        System.out.println("----------------  after returning -----------------------");
        return null;
    }

}
