# MySpring
Spring的简易实现

模仿学习Spring源码，实现了IOC和AOP的基本功能，简化了复杂的继承及接口逻辑，忽略了大部分细节处理，部分类与源码命名相同, 有些类作了命名的简化。

# 功能实现
## IOC
- Bean的扫描，按照`@ComponentScan`指定路径递归扫描文件夹，遇到`@Commponent`注解的类则创建对应`BeanDefinition`加入`BeanDefinitionMap`中.
- Bean的主要生命周期，包括创建、实例化、依赖注入、初始化、Aware接口回调、后置处理器回调等.
- 三级缓存解决单例Bean的循环依赖问题，`singletonObjects`存放成品、`earlySingletonObjects`存放半成品、`singletonFactories`存放早期工厂.

## AOP
- 只支持`@Before`和`@AfterReturning`两种通知.
- 切点的简单匹配，只支持`@annotation`注解的形式.
- `@Aspect`标记的高级Advisor向低级Advisor的转变, 利用`AdvisorFactory`实现, 只支持高级`Advisor`.
- `AnnotationAwareAspectJAutoProxyCreator`后置处理器进行代理的创建, `findCandidateAdvisors()`寻找容器中的所有`Advisor`, 只查找一次然后缓存起来; `findEligibleAdvisors()`查找当前类匹配的`Advisor`; `wrapIfNecessary()`如果有匹配的`Advisor`则创建动态代理, 否则不创建.
- 调用时通过适配器将`Advisor`转化为`Interceptor`, 通过`Interceptor`调用`Advice`.
- `Invocation`进行`Interceptor`的责任链递归调用, 未实现排序功能.
- 通过JDK创建动态代理, 未实现cglib动态代理.
- 循环依赖时通过`getEarlyBeanReference()`提前创建代理, 避免引用到原对象.

