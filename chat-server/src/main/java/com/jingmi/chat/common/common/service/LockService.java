package com.jingmi.chat.common.common.service;
import com.jingmi.chat.common.common.domain.enums.CommonErrorEnum;
import com.jingmi.chat.common.common.exception.BusinessException;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public <T> T executeWithLockThrows(String key, int waitTime, TimeUnit unit, SupplierThrow<T> supplier) throws Throwable {
        RLock lock = redissonClient.getLock(key);
        boolean lockSuccess = lock.tryLock(waitTime, unit);
        if (!lockSuccess) {
            throw new BusinessException(CommonErrorEnum.LOCK_LIMIT);
        }
        try {
            return supplier.get();//执行锁内的代码逻辑
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @SneakyThrows
    public <T> T executeWithLock(String key, int waitTime, TimeUnit unit, Supplier<T> supplier) {
        return executeWithLockThrows(key, waitTime, unit, supplier::get);
    }
    public <T> T executeWithLock(String lockKey ,Runnable runnable)  {

        return executeWithLock(lockKey,-1,TimeUnit.SECONDS,()->{
            runnable.run();
            return null;
        });
    }
        /**
     * 一个功能性接口，定义了一个可以抛出异常的供应商。
     * <p>
     * 此接口的目的是提供一个可用于lambda表达式或方法引用的功能性编程实体。
     * 它代表一个不接受任何参数并返回类型为T的结果的函数。此外，此函数允许抛出任何异常。
     * <p>
     * 此接口通常用于需要在lambda表达式中抛出异常的场景，例如在自定义执行服务中或在特定的功能编程库中。
     *
     * @param <T> 由这个供应商提供的结果的类型。
     */
    @FunctionalInterface
    public interface SupplierThrow<T> {

        /**
         * 获取一个结果。
         * <p>
         * 此方法代表了SupplierThrow接口的核心功能。它不接受任何参数并返回类型为T的结果。
         * 当在lambda表达式或方法引用中实现此方法时，可能抛出的任何异常都应在throws子句中声明。
         *
         * @return 类型为T的结果。
         * @throws Throwable 在执行供应商过程中可能出现的任何异常。
         */
        T get() throws Throwable;
    }

}
