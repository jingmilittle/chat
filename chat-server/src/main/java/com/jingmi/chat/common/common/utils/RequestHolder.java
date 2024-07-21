package com.jingmi.chat.common.common.utils;

import com.jingmi.chat.common.common.domain.dto.RequestInfo;

/**
 * @program: jingmiChat
 * @description:
 * @author: JingMi
 * @create: 2024-07-21 00:03
 **/

public class RequestHolder {
private  static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();
public static void set(RequestInfo requestInfo){
    threadLocal.set(requestInfo);

}
public static RequestInfo get(){

    return threadLocal.get();
}
public static void remove(){
    threadLocal.remove();}
}
