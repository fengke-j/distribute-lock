package com.fk.distribute.lock.config;

import com.fk.distribute.lock.aspect.AbstractLockAspect;
import com.fk.distribute.lock.aspect.DistributedLockAspect;
import com.fk.distribute.lock.aspect.ThreadMethodLockAspect;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengke
 * @date 2025/6/7
 */
@Configuration
//确保redisson bean加载完成
@AutoConfigureOrder(Integer.MAX_VALUE)
public class DistributedLockAutoConfig {

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public AbstractLockAspect distributedLockAspect() {
        return new DistributedLockAspect();
    }

    @Bean
    @ConditionalOnMissingBean(DistributedLockAspect.class)
    public AbstractLockAspect threadMethodLockAspect() {
        return new ThreadMethodLockAspect();
    }
}
