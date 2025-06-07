package com.fk.distribute.lock;

import java.lang.reflect.Parameter;

/**
 * @author fengke
 * @date 2025/6/7
 */
@FunctionalInterface
public interface LockKeyGenerator {

    /**
     * 自定义生成锁的Bean
     */
    String generateLockKey(String prefix, String methodName, Parameter[] parameters, Object[] args);
}
