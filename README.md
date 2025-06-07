# distribute-lock



## 使用指南

### 注解
#### @DistributedLock  （方法注解,启用锁）
##### 参数说明： 
    prefix（锁名前缀）
    waitTime（获取锁最大的等待时间）
    leaseTime（锁的持有时间）
    timeUnit（时间单位）
    lockKeyGeneratorBeanName（生成锁的Bean名称），在LockKeyGeneratorAutoConfig配置类中已实现默认的生成锁bean

#### @ResourceBinding   （参数注解，利用参数生成锁）
##### 说明：
    对于默认的生成锁Bean（LockKeyGeneratorAutoConfig#resourceLockKeyGenerator）的解释：
    此注解只能应用于基本类型及其包装类型（重写toString也可以）；其他类型会发出警告，
    并且会导致锁失效。若没有使用@ResourceBinding注解，则会使用工程名加方法名作为锁key

    eg：在demo服务中，对于订单号（20250120-62533857）

        @DistributedLock
        public void test1(@ResourceBinding String orderNo, ...){...}

        @DistributedLock
        public void test2(@ResourceBinding String orderNo, ...){...}

        ...

        生成的锁Key为：demo:20250120-62533857
        实现效果为：demo服务对于订单号为：20250120-62533857，有且仅有一个方法可以执行

### 说明
    没有启用redis的情况下，则会仅本地线程间同步执行