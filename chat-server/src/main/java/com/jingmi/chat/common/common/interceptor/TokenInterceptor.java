package com.jingmi.chat.common.common.interceptor;

import com.jingmi.chat.common.common.exception.HttpErrorEnum;
import com.jingmi.chat.common.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * @program: jingmiChat
 * @description: 实现token拦截器
 * @author: JingMi
 * @create: 2024-07-20 21:07
 **/
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_ = "Bearer ";
    public static final String PUBLIC = "public";
    public static final String UID = "uid";
    @Autowired
    private LoginService  loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        Long uid = loginService.getValidUid(token);
        if(Objects.nonNull(uid)){
            //登录状态
            request.setAttribute(UID,uid);

        }else {
            boolean isPublicURI = isPublicURI(request);
            if(!isPublicURI){
                //401
                log.info("token is not valid");
                HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
                return false;
            }

        }
        return true;
    }

    private static boolean isPublicURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        boolean isPublicURI = split.length>3&& PUBLIC.equals(split[3]);
        return isPublicURI;
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        return Optional.ofNullable(token)
                .filter(h->h.startsWith(BEARER_))
                .map(h->h.replaceFirst(BEARER_,""))
                .orElse(null);
    }
}
