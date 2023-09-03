package com.fly.spring.aop.proxy;

import com.fly.spring.aop.advisor.Advisor;
import com.fly.spring.aop.adapter.AdvisorAdapter;
import com.fly.spring.aop.adapter.AfterReturningAdviceAdapter;
import com.fly.spring.aop.adapter.BeforeAdviceAdapter;
import com.fly.spring.aop.advice.Advice;
import com.fly.spring.aop.interceptor.Interceptor;
import com.fly.spring.aop.pointcut.MethodMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProxyFactory {

    private Object target;
    private List<Advisor> advisors;
    private List<AdvisorAdapter> adapters;

    public ProxyFactory() {
        this.adapters = new ArrayList<>();
        this.adapters.add(new BeforeAdviceAdapter());
        this.adapters.add(new AfterReturningAdviceAdapter());
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }

    public void addAdvisors(List<Advisor> advisors) {
        if (this.advisors == null) this.advisors = new ArrayList<>();
        this.advisors.addAll(advisors);
    }


    public Object getProxy() {
        // TODO: 判断使用 JDK 动态代理，还是 cglib 动态代理
        return new JdkAopProxy(this).getProxy();
    }

    /**
     * 遍历 advisors 匹配切点，并统一转换为 interceptor
     * @param method
     * @param targetClass
     * @return
     */
    public List<Interceptor> getInterceptors(Method method, Class<?> targetClass) {
        List<Interceptor> interceptors = new ArrayList<>();
        for (Advisor advisor: this.advisors) {
            MethodMatcher methodMatcher = advisor.getPointcut().getMethodMatcher();
            try {
                if (methodMatcher.matches(method, targetClass)) {
                    Advice advice = advisor.getAdvice();
                    for (AdvisorAdapter adapter: this.adapters) {
                        if (adapter.supportsAdvice(advice)) {
                            interceptors.add(adapter.getInterceptor(advisor));
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return interceptors;
    }

}
