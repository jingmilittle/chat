package com.jingmi.chat.common.common.thread;

import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory; /**
 * @program: jingmiChat
 * @description: 线程工厂 内部组合了原工厂 在创建线程时会调用原工厂然后设置异常捕获方式 使用了装饰器模式
 * @author: JingMi
 * @create: 2024-07-18 00:11
 **/
@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory{
    private static final MyUnCaughExceptionHandler  MY_UNCAUGH_EXCEPTION_HANDLER= new MyUnCaughExceptionHandler();
    private ThreadFactory originalFactory;
   @Override
    public Thread newThread(Runnable r){
       Thread thread =originalFactory.newThread(r);
              thread.setUncaughtExceptionHandler(MY_UNCAUGH_EXCEPTION_HANDLER);
       return thread;
   }
}
