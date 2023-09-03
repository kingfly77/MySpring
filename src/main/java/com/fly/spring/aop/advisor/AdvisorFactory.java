package com.fly.spring.aop.advisor;

import com.fly.spring.aop.AspectInstanceFactory;
import com.fly.spring.aop.SimpleAspectInstanceFactory;
import com.fly.spring.aop.advice.AfterReturningAdvice;
import com.fly.spring.aop.advice.BeforeAdvice;
import com.fly.spring.aop.annotations.*;
import com.fly.spring.aop.pointcut.DefaultPointcut;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvisorFactory {

    /**
     * 生产 Advisor，低级 Advisor直接生成，高级 Advisor 转换为低级 Advisor list
     * @param clz
     * @return
     */
    public static List<Advisor> createAdvisors(Class<?> clz) {
        List<Advisor> advisors = new ArrayList<>();
        // 低级 Advisor，直接创建并返回
        if (!clz.isAnnotationPresent(Aspect.class)) {
            try {
                advisors.add((Advisor) clz.newInstance());
                return advisors;
            } catch (Exception e) {
                return advisors;
            }
        }
        // TODO: 高级 Advisor，遍历方法
        Map<String, Method> pointcutMap = new HashMap<>();
        for (Method method: clz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Pointcut.class)) {
                pointcutMap.put(method.getName(), method);
            }
        }
        AspectInstanceFactory aif = new SimpleAspectInstanceFactory(clz);
        for (Method method: clz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                // pointcut
                String pointcutName = method.getAnnotation(Before.class).value();
                pointcutName = pointcutName.substring(0, pointcutName.indexOf("("));
                Method pointcutMethod = pointcutMap.get(pointcutName);
                if (pointcutMethod == null) continue;
                String expression = pointcutMethod.getAnnotation(Pointcut.class).value();
                DefaultPointcut pointcut = new DefaultPointcut();
                pointcut.setExpression(expression);
                // advice
                BeforeAdvice advice = new BeforeAdvice(method, pointcut, aif);
                // advisor
                Advisor advisor = new DefaultAdvisor(pointcut, advice);
                advisors.add(advisor);
            } else if (method.isAnnotationPresent(AfterReturning.class)) {
                // pointcut
                String pointcutName = method.getAnnotation(AfterReturning.class).value();
                pointcutName = pointcutName.substring(0, pointcutName.indexOf("("));
                Method pointcutMethod = pointcutMap.get(pointcutName);
                if (pointcutMethod == null) continue;
                String expression = pointcutMethod.getAnnotation(Pointcut.class).value();
                DefaultPointcut pointcut = new DefaultPointcut();
                pointcut.setExpression(expression);
                // advice
                AfterReturningAdvice advice = new AfterReturningAdvice(method, pointcut, aif);
                // advisor
                Advisor advisor = new DefaultAdvisor(pointcut, advice);
                advisors.add(advisor);
            }
            // TODO: Around
        }
        return advisors;
    }

}
