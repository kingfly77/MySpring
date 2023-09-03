package com.fly.spring.aop.invocation;

import com.fly.spring.aop.interceptor.Interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class DefaultInvocation implements Invocation {

    public DefaultInvocation(Object target, Method method, Object[] args, List<Interceptor> interceptors) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.interceptors = interceptors;
    }

    private Object target;
    private Method method;
    private Object[] args;

    private List<Interceptor> interceptors;

    private int currentInterceptorIndex = -1;




    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return args;
    }

    @Override
    public Object getThis() {
        return target;
    }

    /**
     * 责任链递归调用 interceptors
     * before -> before -> before
     *                        |
     *                     target.method
     *                        |
     * after  <- after  <-  after
     * @return
     * @throws Throwable
     */
    @Override
    public Object proceed() throws Throwable {
        // 到达链尾后，执行 target.method 方法
        if (this.currentInterceptorIndex == this.interceptors.size() - 1) {
            return method.invoke(this.target, this.args);
        }
        Interceptor interceptor = this.interceptors.get(++this.currentInterceptorIndex);
        return interceptor.invoke(this);
    }
}
