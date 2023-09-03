package com.fly.spring.aop;

import com.fly.spring.aop.AspectInstanceFactory;

public class SimpleAspectInstanceFactory implements AspectInstanceFactory {

    private final Class<?> aspectClass;

    public SimpleAspectInstanceFactory(Class<?> aspectClass) {
        this.aspectClass = aspectClass;
    }

    @Override
    public Object getAspectInstance() {
        try {
            return this.aspectClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
