package com.jingmi.chat.common.common.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.jingmi.chat.common.common.domain.dto.RequestInfo;
import com.jingmi.chat.common.common.utils.RequestHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @program: jingmiChat
 * @description: 收集拦截器
 * @author: JingMi
 * @create: 2024-07-20 23:45
 **/
@Component
public class CollectorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Long uid = Optional.ofNullable(request.getAttribute(TokenInterceptor.UID)).map(Object::toString)
                .map(Long::parseLong).orElse(null);
        String ip = ServletUtil.getClientIP(request);
        RequestInfo requestInfo = RequestInfo.builder().ip(ip).uid(uid).build();
        RequestHolder.set(requestInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
    }
}
