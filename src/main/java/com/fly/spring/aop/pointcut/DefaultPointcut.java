package com.fly.spring.aop.pointcut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class DefaultPointcut implements Pointcut, MethodMatcher {

    private String expression;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }


    /**
     * 判断 pointcut 与 targetClass 类的 method 方法是否匹配
     * @param method
     * @param targetClass
     * @return
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) throws ClassNotFoundException {
        if (this.expression.startsWith("execution")) {
            // TODO: execution
            return false;
        } else if (this.expression.startsWith("@annotation")) {
            // @annotation
            String annoName = expression.substring(expression.indexOf("(") + 1, expression.indexOf(")"));
            if (!Annotation.class.isAssignableFrom(Class.forName(annoName))) return false;
            Class<? extends Annotation> annoClass = (Class<? extends Annotation>) Class.forName(annoName);
            boolean flag = method.isAnnotationPresent(annoClass);
            return flag;
        } else return false;
    }
}
