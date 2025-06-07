package com.fk.distribute.lock.aspect;

import com.fk.distribute.lock.LockKeyGenerator;
import com.fk.distribute.lock.annocation.DistributedLock;
import jakarta.annotation.Nonnull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import java.lang.reflect.Parameter;

/**
 * @author fengke
 * @date 2025/6/7
 */
@Aspect
public abstract class AbstractLockAspect implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Pointcut("@annotation(com.fk.distribute.lock.annocation.DistributedLock)")
    public void lockPointCut() {}

    private Parameter[] getParameters(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        return joinPoint.getTarget().getClass()
                .getMethod(joinPoint.getSignature().getName(), getMethodParameterTypes(joinPoint))
                .getParameters();
    }

    private Class<?>[] getMethodParameterTypes(ProceedingJoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
    }

    protected DistributedLock getDistributedLock(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        return joinPoint.getTarget().getClass()
                .getMethod(joinPoint.getSignature().getName(), getMethodParameterTypes(joinPoint))
                .getAnnotation(DistributedLock.class);
    }

    protected String getLockName(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        // 获取方法上的注解
        DistributedLock distributedLock = getDistributedLock(joinPoint);
        Object[] args = joinPoint.getArgs();
        String prefix = distributedLock.prefix();
        if("".equals(prefix)) {
            Environment env = applicationContext.getEnvironment();
            prefix = env.getProperty("spring.application.name");
        }
        //获取生成锁的bean对象
        LockKeyGenerator lockKeyGenerator = applicationContext.getBean(distributedLock.lockKeyGeneratorBeanName(), LockKeyGenerator.class);
        // 使用锁名生成器生成锁名
        return lockKeyGenerator.generateLockKey(prefix, joinPoint.getSignature().getName(), getParameters(joinPoint), args);
    }

    @Around("lockPointCut()")
    public abstract Object doAround(ProceedingJoinPoint joinPoint) throws Throwable;
}
