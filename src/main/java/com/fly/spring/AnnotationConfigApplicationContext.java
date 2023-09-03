package com.fly.spring;

import com.fly.spring.annotations.Autowired;
import com.fly.spring.annotations.Component;
import com.fly.spring.annotations.ComponentScan;
import com.fly.spring.annotations.Scope;
import com.fly.spring.aop.AnnotationAwareAspectJAutoProxyCreator;
import com.fly.spring.interfaces.*;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationConfigApplicationContext {

    private Class<?> configClass;

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    // 三级缓存
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();
    private Map<String, ObjectFactory<?>> singletonFactories = new ConcurrentHashMap<>();

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public AnnotationConfigApplicationContext(Class<?> configClass) {
        this.configClass = configClass;

        // 扫描路径生成 BeanDefinitionMap
        this.scan();

        // 注册后置处理器
        this.registerBeanPostProcessor();

        System.out.println("application context init done.");
    }

    /**
     * 注册后置处理器到 beanPostProcessorList
     */
    private void registerBeanPostProcessor() {
        // 先注册常用后置处理器（如：代理类生成器）
        this.registerCommonBeanPostProcessors();

        // 遍历 BeanDefinitionMap，查找、创建、注册后置处理器
        for (String beanName: beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (BeanPostProcessor.class.isAssignableFrom(beanDefinition.getType())) {
                BeanPostProcessor beanPostProcessor = (BeanPostProcessor) this.getBean(beanName);
                this.beanPostProcessorList.add(beanPostProcessor);
            }
        }
    }

    /**
     * 注册常用后置处理器
     */
    private void registerCommonBeanPostProcessors() {
        // 注册代理生成器
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setType(AnnotationAwareAspectJAutoProxyCreator.class);
        beanDefinition.setScope("singleton");
        this.beanDefinitionMap.put("proxyCreator", beanDefinition);
        BeanPostProcessor instance = new AnnotationAwareAspectJAutoProxyCreator(this);
        this.singletonObjects.put("proxyCreator", instance);
        this.beanPostProcessorList.add(instance);
    }


    /**
     * 扫描路径并生成 beanDefinitionMap
     */
    private void scan() {
        if (this.configClass == null) return;
        if (!this.configClass.isAnnotationPresent(ComponentScan.class)) {
            throw new RuntimeException("need annotation: @ComponentScan.");
        }
        ComponentScan componentScan = configClass.getAnnotation(ComponentScan.class);
        String path = componentScan.value().replace(".", "/");
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            System.out.println("invalid path: " + path);
            return;
        }
        System.out.println("scan path: " + path);
        File dir = new File(resource.getFile());
        scanFile(dir, classLoader);
    }

    public void scanFile(File file, ClassLoader classLoader) {
        if (file.isDirectory()) {
            // 如果是目录，递归扫描
            File[] files = file.listFiles();
            for (File subFile: files) {
                scanFile(subFile, classLoader);
            }
        } else {
            String filePath = file.getAbsolutePath();
            filePath = filePath.replace(File.separator, ".");
            if (!filePath.contains("com.") || !filePath.endsWith(".class")) return;
            filePath = filePath.substring(filePath.indexOf("com."), filePath.lastIndexOf(".class"));
            System.out.println("found path: " + filePath);
            try {
                Class<?> clazz = classLoader.loadClass(filePath);
                if (!clazz.isAnnotationPresent(Component.class)) return;
                Component component = clazz.getAnnotation(Component.class);
                String beanName = component.value();
                if ("".equals(beanName)) beanName = Introspector.decapitalize(clazz.getSimpleName());
                System.out.println("bean name: " + beanName);

                // 如果是后置处理器，加入到 beanPostProcessorList 中
                boolean isPostProcessor = false;
                if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                    isPostProcessor = true;
                    BeanPostProcessor beanPostProcessor = (BeanPostProcessor) clazz.newInstance();
                    beanPostProcessorList.add(beanPostProcessor);
                }

                // 加入 beanDefinitionMap
                BeanDefinition beanDefinition = new BeanDefinition();
                beanDefinition.setType(clazz);
                if (!isPostProcessor && clazz.isAnnotationPresent(Scope.class)) {
                    Scope scopeAnno = clazz.getAnnotation(Scope.class);
                    String scope = scopeAnno.value();
                    beanDefinition.setScope(scope);
                } else {
                    beanDefinition.setScope("singleton");
                }
                beanDefinitionMap.put(beanName, beanDefinition);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 根据 beanName 获取 bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new RuntimeException("not such bean: " + beanName);
        }
        String scope = beanDefinition.getScope();
        if ("singleton".equals(scope)) {
            // 单例 bean 先尝试从三级缓存中取，没有再创建
            Object bean = singletonObjects.get(beanName);
            if (bean == null) {
                bean = earlySingletonObjects.get(beanName);
                if (bean == null) {
                    ObjectFactory<?> objectFactory = singletonFactories.get(beanName);
                    if (objectFactory != null) {
                        // 工厂中有，则通过工厂创建 bean，并移入二级缓存
                        bean = objectFactory.getObject();
                        this.earlySingletonObjects.put(beanName, bean);
                        this.singletonFactories.remove(beanName);
                    }
                }
            }
            // 如果在三级缓存中都没有找到 bean，则创建 bean
            if (bean == null) {
                bean = createBean(beanName, beanDefinition);
            }
            return bean;
        } else {
            // 原型 bean 直接创建
            return createBean(beanName, beanDefinition);
        }
    }

    public <T> T getBean(String beanName, Class<T> clz) {
        try {
            return (T) getBean(beanName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据 beanName 和 beanDefinition 创建 bean
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getType();
        // 实例化
        Object bean = null;
        try {
            bean = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 实例化之后，依赖注入之前：如果是单例 Bean，先创建工厂放入 singletonFactories 中，等待后续可能的回调
        if ("singleton".equals(beanDefinition.getScope())) {
            Object temp = bean;
            this.singletonFactories.put(beanName, new ObjectFactory<Object>() {
                @Override
                public Object getObject() {
                    Object retObject = temp;
                    // 遍历后置处理器，找到实现了 SmartInstantiation... 接口的后置处理器
                    for (BeanPostProcessor beanPostProcessor: AnnotationConfigApplicationContext.this.beanPostProcessorList) {
                        if (beanPostProcessor instanceof SmartInstantiationAwareBeanPostProcessor) {
                            retObject = ((SmartInstantiationAwareBeanPostProcessor) beanPostProcessor).getEarlyBeanReference(temp, beanName);
                        }
                    }
                    return retObject;
                }
            });
        }

        // 属性设置（依赖注入）
        populate(bean, clazz);

        // 如果在二级缓存中存在，说明发生了循环引用，应该把bean替换为二级缓存中的bean
        if ("singleton".equals(beanDefinition.getScope())) {
            Object earlyReference = earlySingletonObjects.get(beanName);
            if (earlyReference != null) {
                bean = earlyReference;
            }
        }

        // Awares
        if (bean instanceof BeanNameAware) {
            ((BeanNameAware) bean).setBeanName(beanName);
        }

        // 初始化前
        for (BeanPostProcessor beanPostProcessor: beanPostProcessorList) {
            try {
                bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }



        // 初始化（afterPropertiesSet）
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }

        // 初始化后
        for (BeanPostProcessor beanPostProcessor: beanPostProcessorList) {
            try {
                bean = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        // 初始化后，如果是单例Bean：1. 加入到成品池中， 2. 删除其他两个池中的缓存
        if (bean != null && "singleton".equals(beanDefinition.getScope())) {
            singletonObjects.put(beanName, bean);
            earlySingletonObjects.remove(beanName);
            singletonFactories.remove(beanName);
        }
        return bean;
    }

    /**
     * 属性设置（依赖注入）
     * @param bean
     * @param clazz
     */
    private void populate(Object bean, Class<?> clazz) {
        // 依赖注入
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                try {
                    Object value = getBean(field.getName());
                    field.set(bean, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }
}
