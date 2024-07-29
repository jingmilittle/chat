package com.jingmi.chat.common.common.aspect;

import cn.hutool.core.util.StrUtil;
import com.jingmi.chat.common.common.annotation.RedissonLock;
import com.jingmi.chat.common.common.service.LockService;
import com.jingmi.chat.common.common.utils.SpElUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

/**
 * @program: jingmiChat
 * @description: redis注解切面
 * @author: JingMi
 * @create: 2024-07-29 22:29
 **/
@Component
@Aspect
/**
 * 确保比事务先执行
 */
@Order(0)
public class RedissonLockAspect {
    @Autowired
    private LockService lockService;
    /**
     * 基于RedissonLock注解的切面环绕通知。
     * 在方法执行前尝试获取锁，确保同一时间只有一个实例能执行该方法，以实现线程安全。
     * 使用Redisson客户端提供的锁功能来实现。
     *
     * @param joinPoint 切点，包含目标方法的信息。
     * @return 目标方法的返回值。
     * @throws Throwable 如果方法执行过程中抛出异常。
     */
    @Around("@annotation(com.jingmi.chat.common.common.annotation.RedissonLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取目标方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 从方法上获取RedissonLock注解
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        // 根据注解的prefix字段和方法信息生成锁的前缀，如果prefix为空，则使用方法的关键字作为前缀
        String prefix = StrUtil.isBlank(redissonLock.prefix()) ? SpElUtils.getMethodKey(method) : redissonLock.prefix();
        // 使用SpEL表达式解析出锁的键，结合方法参数动态生成锁的唯一标识
        String prefixKey = prefix + ":" + SpElUtils.parseSpEl(method, joinPoint.getArgs(), redissonLock.key());
        // 通过锁服务，使用定义好的锁前缀和键，以及注解中配置的等待时间和时间单位，尝试获取锁并执行目标方法
        return lockService.executeWithLockThrows(prefixKey, redissonLock.waitTime(), redissonLock.unit(), joinPoint::proceed);
    }


}
