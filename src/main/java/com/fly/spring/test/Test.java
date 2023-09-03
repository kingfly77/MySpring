package com.fly.spring.test;

import com.fly.spring.AnnotationConfigApplicationContext;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = (UserService) applicationContext.getBean("userService");
        User user = (User) applicationContext.getBean("user");
        user.setId(999);
        user.setName("hello");
        userService.test();
    }

}
