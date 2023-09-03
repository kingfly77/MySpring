package com.fly.spring.interfaces;

/**
 * 对象工厂，缓存于 singletonFactories 中
 * @param <T>
 */

public interface ObjectFactory<T> {

    T getObject();

}
