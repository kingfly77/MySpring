package com.fly.spring.test.circularDependencyTest;

import com.fly.spring.annotations.Autowired;
import com.fly.spring.annotations.Component;
import com.fly.spring.annotations.Scope;
import com.fly.spring.test.MyPointcut;

@Component("a")
public class A implements Base {

    private int id;

    @Autowired
    private Base b;

    @Override
    @MyPointcut2
    public void test() {
        System.out.println("A test");
    }
}
