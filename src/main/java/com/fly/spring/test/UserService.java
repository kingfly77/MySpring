package com.fly.spring.test;

import com.fly.spring.interfaces.BeanNameAware;

public interface UserService {

    @MyPointcut
    public void test();

}
