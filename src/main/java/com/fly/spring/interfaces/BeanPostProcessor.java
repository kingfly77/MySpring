package com.fly.spring.interfaces;

/**
 * 后置处理器
 */

public interface BeanPostProcessor {

    /**
     * 初始化前回调
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws Throwable;


    /**
     * 初始化后回调
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws Throwable;

}
