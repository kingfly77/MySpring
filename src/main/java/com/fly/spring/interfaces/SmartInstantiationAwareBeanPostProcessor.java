package com.fly.spring.interfaces;

/**
 * 实现该接口的后置处理器，才能在循环依赖时保证引用的一致
 */

public interface SmartInstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 获取 bean 的早期引用
     * @param bean
     * @param beanName
     * @return
     */
    Object getEarlyBeanReference(Object bean, String beanName);

}
