package com.fly.spring.test.circularDependencyTest;

public interface Base {

    @MyPointcut2
    void test();

}
