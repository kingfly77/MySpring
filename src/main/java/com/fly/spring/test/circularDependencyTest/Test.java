package com.fly.spring.test.circularDependencyTest;

import com.fly.spring.AnnotationConfigApplicationContext;
import com.fly.spring.test.AppConfig;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        Base a = (Base) applicationContext.getBean("a");
        Base b = (Base) applicationContext.getBean("b");
//        System.out.println(a.b == b);
        a.test();
        b.test();
    }



}
