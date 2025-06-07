package com.fk.distribute.lock.config;

import com.fk.distribute.lock.LockKeyGenerator;
import com.fk.distribute.lock.annocation.ResourceBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Parameter;

/**
 * @author fengke
 * @date 2025/6/7
 */
@AutoConfiguration
@Configuration
public class LockKeyGeneratorAutoConfig {

    private static final Logger log = LoggerFactory.getLogger(LockKeyGeneratorAutoConfig.class);

    @Bean("resourceLockKeyGenerator")
    public LockKeyGenerator resourceLockKeyGenerator() {
        return (prefix, methodName, parameters, args) -> {
            StringBuilder sb = new StringBuilder();
            if(parameters.length > 0 && args.length > 0) {
                for(int i = 0; i < parameters.length; i++) {
                    boolean isPresent = parameters[i].isAnnotationPresent(ResourceBinding.class);
                    if(isPresent) {
                        warnLockInvalidate(parameters[i]);
                        sb.append(args[i]);
                    }
                }
            }
            if(sb.toString().isEmpty()) {
                sb.append(methodName);
            }
            return prefix + ":" + sb;
        };
    }

    private void warnLockInvalidate(Parameter parameter) {
        Class<?> type = parameter.getType();
        if(type.isPrimitive() || type.equals(String.class) || type.equals(Integer.class) ||
                type.equals(Long.class) || type.equals(Short.class) || type.equals(Byte.class) ||
                type.equals(Boolean.class) || type.equals(Character.class) || type.equals(Float.class) ||
                type.equals(Double.class)) {} else {
            log.warn("Using non-primitive types or their wrapper classes can lead to lock invalidation.");
        }
    }
}
