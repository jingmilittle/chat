package com.jingmi.chat.common.common.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: jingmiChat
 * @description: 线程异常处理
 * @author: JingMi
 * @create: 2024-07-18 00:14
 **/
@Slf4j
public class MyUnCaughExceptionHandler implements Thread.UncaughtExceptionHandler{
   @Override
    public void uncaughtException(Thread t, Throwable e) {
     log.error("线程{}发生异常，异常信息为：{}",t.getName(),e.getMessage());
    }

}
