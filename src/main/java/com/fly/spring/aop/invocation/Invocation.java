package com.fly.spring.aop.invocation;

import java.lang.reflect.Method;

public interface Invocation {

    Method getMethod();

    Object[] getArguments();

    Object getThis();

    Object proceed() throws Throwable;

}
