package com.fk.distribute.lock.aspect;

import com.fk.distribute.lock.annocation.DistributedLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author fengke
 * @date 2025/6/7
 */
@Aspect
public class DistributedLockAspect extends AbstractLockAspect{

    private static final Logger log = LoggerFactory.getLogger(DistributedLockAspect.class);

    @Override
    @Around("lockPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        RedissonClient redissonClient = applicationContext.getBean(RedissonClient.class);
        return useDistributedLock(joinPoint, redissonClient);
    }

    private Object useDistributedLock(ProceedingJoinPoint joinPoint, RedissonClient redissonClient) throws Throwable {
        DistributedLock distributedLock = getDistributedLock(joinPoint);
        String lockName = getLockName(joinPoint);
        String label = UUID.randomUUID().toString().replace("-", "");
        //获取锁
        RLock lock = redissonClient.getLock(lockName);

        try{
            log.debug("Attempting to acquire lock for label:{} {}", label,lockName);
            //尝试获取锁
            boolean isLocked = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if(isLocked){
                log.debug("Lock acquired for label:{} {}",label, lockName);
                try {
                    //执行方法
                    return joinPoint.proceed();
                } finally {
                    log.debug("Lock released for label:{} {}", label,lockName);
                    //释放锁
                    lock.unlock();
                }
            } else {
                log.warn("Could not acquire lock for label:{} {}", label,lockName);
                // 获取锁失败，抛出异常或返回特定结果
                throw new RuntimeException("Could not acquire lock for " + lockName);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Lock interrupted for " + lockName, e);
        }
    }
}
