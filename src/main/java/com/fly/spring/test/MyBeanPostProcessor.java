package com.fly.spring.test;

import com.fly.spring.annotations.Component;
import com.fly.spring.interfaces.BeanPostProcessor;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("before initialization: " + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("after initialization: " + beanName);
        return bean;
    }
}
