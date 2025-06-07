package com.fk.distribute.lock.demo.service;

import com.fk.distribute.lock.annocation.DistributedLock;
import com.fk.distribute.lock.annocation.ResourceBinding;
import org.springframework.stereotype.Service;

/**
 * @author fengke
 * @date 2025/6/7
 */
@Service
public class TestDistributeLockService {

    @DistributedLock
    public String testDistributeLock(@ResourceBinding String name) throws InterruptedException {
        Thread.sleep(10000);
        return name;
    }
}
