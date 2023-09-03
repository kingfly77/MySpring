package com.fly.spring.aop;

import com.fly.spring.AnnotationConfigApplicationContext;
import com.fly.spring.BeanDefinition;
import com.fly.spring.aop.advisor.AdvisorFactory;
import com.fly.spring.aop.annotations.Aspect;
import com.fly.spring.aop.advisor.Advisor;
import com.fly.spring.aop.pointcut.MethodMatcher;
import com.fly.spring.aop.proxy.ProxyFactory;
import com.fly.spring.interfaces.BeanPostProcessor;
import com.fly.spring.interfaces.SmartInstantiationAwareBeanPostProcessor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationAwareAspectJAutoProxyCreator implements SmartInstantiationAwareBeanPostProcessor {

    private AnnotationConfigApplicationContext appContext;

    private List<Advisor> candidateAdvisors;

    private final Map<String, Object> earlyProxyReferences = new ConcurrentHashMap<>();

    public AnnotationAwareAspectJAutoProxyCreator(AnnotationConfigApplicationContext appContext) {
        this.appContext = appContext;
    }

    /**
     * 寻找合适的 advisors
     * @param beanClass
     * @param beanName
     * @return
     */
    private List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) throws Throwable {
        if (this.candidateAdvisors == null) {
            // 寻找所有候选 advisors
            findCandidateAdvisors();
        }
        // 遍历筛选 advisor
        if (this.candidateAdvisors.isEmpty()) return this.candidateAdvisors;
        List<Advisor> eligibleAdvisors = new ArrayList<>();
        Method[] methods = beanClass.getDeclaredMethods();
        for (Advisor advisor: this.candidateAdvisors) {
            MethodMatcher methodMatcher = advisor.getPointcut().getMethodMatcher();
            for (Method method: methods) {
                if (methodMatcher.matches(method, beanClass)) {
                    eligibleAdvisors.add(advisor);
                    break;
                }
            }
        }
        return eligibleAdvisors;
    }

    /**
     * 寻找所有候选 advisors
     * @return
     */
    private void findCandidateAdvisors() {
        // 寻找低级 advisors
        List<Advisor> advisors = this.findLowLevelAdvisors();
        // 寻找高级 advisors
        advisors.addAll(this.findHighLevelAdvisors());
        System.out.println("candidate advisors: ");
        for (Advisor advisor: advisors) {
            System.out.println(advisor);
        }
        this.candidateAdvisors = advisors;
    }

    /**
     * 寻找低级 advisors (Advisor Bean)
     * @return
     */
    private List<Advisor> findLowLevelAdvisors() {
        // TODO
        return new ArrayList<>();
    }

    /**
     * 寻找高级 advisors (@Aspect bean)
     * @return
     */
    private List<Advisor> findHighLevelAdvisors() {
        List<Advisor> advisors = new ArrayList<>();
        Map<String, BeanDefinition> beanDefinitionMap = this.appContext.getBeanDefinitionMap();
        for (String beanName: beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            Class<?> clz = beanDefinition.getType();
            if (clz.isAnnotationPresent(Aspect.class)) {
                advisors.addAll(AdvisorFactory.createAdvisors(clz));
            }
        }
        return advisors;
    }


    /**
     * 有需要则创建代理，否则返回原对象
     * @param bean
     * @param beanName
     * @return
     */
    private Object wrapIfNecessary(Object bean, String beanName) {
        try {
            // 寻找对应的advisors，如果没有则返回原对象，否则创建代理
            List<Advisor> advisors = findEligibleAdvisors(bean.getClass(), beanName);
            if (advisors.isEmpty()) return bean;
            return createProxy(bean, beanName, advisors);
        } catch (Throwable e) {
            return null;
        }
    }

    private Object createProxy(Object bean, String beanName, List<Advisor> advisors) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(bean);
        proxyFactory.addAdvisors(advisors);
        return proxyFactory.getProxy();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // 判断是否已经创建过代理，已经创建过直接返回
        if (this.earlyProxyReferences.get(beanName) == null) {
            return this.wrapIfNecessary(bean, beanName);
        } else {
            return bean;
        }
    }


    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        this.earlyProxyReferences.put(beanName, bean);
        return wrapIfNecessary(bean, beanName);
    }
}
