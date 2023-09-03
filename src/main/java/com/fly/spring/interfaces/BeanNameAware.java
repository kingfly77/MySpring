package com.fly.spring.interfaces;

/**
 * 用于获取 beanName 的回调接口
 */

public interface BeanNameAware {

    void setBeanName(String beanName);

}
