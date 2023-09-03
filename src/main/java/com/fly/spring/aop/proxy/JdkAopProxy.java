package com.fly.spring.aop.proxy;

import com.fly.spring.aop.interceptor.Interceptor;
import com.fly.spring.aop.invocation.DefaultInvocation;
import com.fly.spring.aop.invocation.Invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class JdkAopProxy implements AopProxy, InvocationHandler {

    private final ProxyFactory proxyFactory;

    private Object target;
    private Class<?>[] interfaces;

    public JdkAopProxy(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
        this.target = proxyFactory.getTarget();
        this.interfaces = proxyFactory.getTarget().getClass().getInterfaces();
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), this.interfaces, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取 interceptor 调用链
        List<Interceptor> chain = this.proxyFactory.getInterceptors(method, target.getClass());
        // 创建 Invocation，调用 interceptors
        Invocation invocation = new DefaultInvocation(this.target, method, args, chain);
        return invocation.proceed();
    }
}
