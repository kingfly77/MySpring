package com.fly.spring.aop.advisor;

import com.fly.spring.aop.advice.Advice;
import com.fly.spring.aop.pointcut.Pointcut;

public class DefaultAdvisor implements Advisor{

    private Pointcut pointcut;
    private Advice advice;

    public DefaultAdvisor(Pointcut pointcut, Advice advice) {
        this.pointcut = pointcut;
        this.advice = advice;
    }


    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

}
