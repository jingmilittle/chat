package com.jingmi.chat.common.common.service;

import com.jingmi.chat.common.common.domain.enums.CommonErrorEnum;
import com.jingmi.chat.common.common.exception.BusinessException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @program: jingmiChat
 * @description:
 * @author: JingMi
 * @create: 2024-07-25 22:45
 **/
@Service
public class LockService {
    @Autowired
    private RedissonClient redissonClient;
    public <T> T executeWithLock(String lockKey, long waitTime, TimeUnit unit, Supplier<T> supplier)  {
        RLock lock = redissonClient.getLock(lockKey);
        boolean success = false;
        try {
            success = lock.tryLock(waitTime, unit);
        } catch (InterruptedException e) {
            throw new BusinessException("获取锁异常");
        }
        if (!success){
            throw  new BusinessException(CommonErrorEnum.LOCK_LIMIT);
        }
        try{

            return supplier.get();
        } finally {
            lock.unlock();

        }

    }
    public <T> T executeWithLock(String lockKey ,Supplier<T> supplier)  {

       return executeWithLock(lockKey,-1,TimeUnit.SECONDS,supplier);
    }
    public <T> T executeWithLock(String lockKey ,Runnable runnable)  {

        return executeWithLock(lockKey,-1,TimeUnit.SECONDS,()->{
            runnable.run();
            return null;
        });
    }


}
