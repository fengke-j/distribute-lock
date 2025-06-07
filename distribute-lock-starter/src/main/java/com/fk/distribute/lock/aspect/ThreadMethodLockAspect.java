package com.fk.distribute.lock.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author fengke
 * @date 2025/6/7
 */
@Aspect
public class ThreadMethodLockAspect extends AbstractLockAspect{

    private static final Logger log = LoggerFactory.getLogger(ThreadMethodLockAspect.class);

    private final ConcurrentMap<String, LockWrapper> lockMap = new ConcurrentHashMap<>();

    private final long EXPIRE_TIME_MS = 60 * 1000;

    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "LockCleaner");
        t.setDaemon(true);
        return t;
    });

    public ThreadMethodLockAspect() {
        cleaner.scheduleAtFixedRate(this::cleanUp, EXPIRE_TIME_MS, EXPIRE_TIME_MS, TimeUnit.MILLISECONDS);
    }

    private void cleanUp() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, LockWrapper>> iter = lockMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, LockWrapper> entry = iter.next();
            LockWrapper wrapper = entry.getValue();
            if (now - wrapper.lastUsed > EXPIRE_TIME_MS) {
                iter.remove();
                log.info("Removed expired lock for key: {}", entry.getKey());
            }
        }
    }

    @Override
    @Around("lockPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return useThreadMethodLock(joinPoint);
    }

    private Object useThreadMethodLock(ProceedingJoinPoint joinPoint) throws Throwable {
        String lockName = getLockName(joinPoint);
        log.info("lockName: {}", lockName);

        LockWrapper lockWrapper = lockMap.compute(lockName, (key, oldWrapper) -> {
            if (oldWrapper == null) {
                return new LockWrapper();
            } else {
                oldWrapper.lastUsed = System.currentTimeMillis();
                return oldWrapper;
            }
        });

        synchronized (lockWrapper.lock) {
            return joinPoint.proceed();
        }
    }

    private static class LockWrapper {
        final Object lock = new Object();
        volatile long lastUsed = System.currentTimeMillis();
    }
}
