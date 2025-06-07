package com.fk.distribute.lock.annocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author fengke
 * @date 2025/6/7
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 锁名前缀
     */
    String prefix() default "";

    /**
     * 获取锁最大的等待时间
     */
    long waitTime() default 30;

    /**
     * 锁的持有时间
     */
    long leaseTime() default 10;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 生成锁的Bean名称
     */
    String lockKeyGeneratorBeanName() default "resourceLockKeyGenerator";
}
