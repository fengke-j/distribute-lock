package com.fk.distribute.lock.demo.controller;

import com.fk.distribute.lock.demo.service.TestDistributeLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengke
 * @date 2025/6/7
 */
@RestController
@RequestMapping
public class TestDistributeLockController {

    @Autowired
    private TestDistributeLockService testDistributeLockService;

    @GetMapping("/test1/distribute/lock")
    public String testDistributeLock1(@RequestParam("testName") String testName) throws InterruptedException {
        return testDistributeLockService.testDistributeLock(testName);
    }

    @GetMapping("/test2/distribute/lock")
    public String testDistributeLock2(@RequestParam("testName") String testName) throws InterruptedException {
        return testDistributeLockService.testDistributeLock(testName);
    }
}
