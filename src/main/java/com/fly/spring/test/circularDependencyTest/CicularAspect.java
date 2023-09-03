package com.fly.spring.test.circularDependencyTest;

import com.fly.spring.annotations.Component;
import com.fly.spring.aop.annotations.Aspect;
import com.fly.spring.aop.annotations.Before;
import com.fly.spring.aop.annotations.Pointcut;
import com.fly.spring.aop.invocation.Invocation;

@Aspect
@Component
public class CicularAspect {

    @Pointcut("@annotation(com.fly.spring.test.circularDependencyTest.MyPointcut2)")
    public void pointcut() {}

    @Before("pointcut()")
    public void before() {
        System.out.println("--------------- before ----------------------");

    }

}
