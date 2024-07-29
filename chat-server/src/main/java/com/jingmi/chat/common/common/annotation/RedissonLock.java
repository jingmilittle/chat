package com.jingmi.chat.common.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
* 分布式锁注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedissonLock {
    /**
    * key前缀 默认取方法全限定名 可以自己指定
     */

    String prefix() default "";
    /**
    * 支持el表达式
     */

    String key();
    /**
    * 等待锁的事件
     */

    int waitTime () default -1;
    /**
    * 锁时间单位
     */
    TimeUnit unit() default  TimeUnit.SECONDS;

}
