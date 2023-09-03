package com.fly.spring.test;

import com.fly.spring.annotations.Autowired;
import com.fly.spring.annotations.Component;
import com.fly.spring.annotations.Scope;
import com.fly.spring.interfaces.BeanNameAware;
import com.fly.spring.interfaces.InitializingBean;

@Component("userService")
@Scope("singleton")
public class UserServiceImpl implements UserService, BeanNameAware, InitializingBean {

    @Autowired
    private User user;

    private String beanName;

    @Override
    @MyPointcut
    public void test() {
        System.out.println("user: " + user);
        System.out.println("bean name aware: " + beanName);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("afterPropertiesSet");
    }
}
