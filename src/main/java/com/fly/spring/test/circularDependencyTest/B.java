package com.fly.spring.test.circularDependencyTest;

import com.fly.spring.annotations.Autowired;
import com.fly.spring.annotations.Component;
import com.fly.spring.annotations.Scope;
import com.fly.spring.test.MyPointcut;

@Component("b")
public class B implements Base {

    private int id;

    @Autowired
    private Base a;

    @Override
    @MyPointcut2
    public void test() {
        System.out.println("B test");
    }
}
