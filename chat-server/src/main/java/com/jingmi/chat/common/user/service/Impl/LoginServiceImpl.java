package com.jingmi.chat.common.user.service.Impl;

import com.jingmi.chat.common.common.config.ThreadPoolConfig;
import com.jingmi.chat.common.common.constant.RedisKey;
import com.jingmi.chat.common.common.utils.JwtUtils;
import com.jingmi.chat.common.common.utils.RedisUtils;
import com.jingmi.chat.common.user.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @program: jingmiChat
 * @description: 登录模块
 * @author: JingMi
 * @create: 2024-07-14 16:17
 **/
@Service
public class LoginServiceImpl implements LoginService {
    public static final int TOKEN_EXPIRE_TIME = 3;
    public static final int NULL_TOKEN = 2;
    public static final int NEED_UPDATE = 1;
    @Autowired
    private JwtUtils jwtUtils;
//    @Autowired
//    @Qualifier(ThreadPoolConfig.CHAT_EXECUTOR)
//    private ThreadPoolExecutor executor;



    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        //中心化存储
        RedisUtils.set(getUserTokenKey(uid), token, TOKEN_EXPIRE_TIME, TimeUnit.DAYS);

        return token ;
    }

    
    /** 
    * @Description: 获取token的key
    * @Param: 
    * @return: 
    * @Author: jingmi
    * @Date: 2024/7/17
    */
    
    @NotNull
    private static String getUserTokenKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
    }

    
    /** 
    * @Description: 获取有效uid
    * @Param: 
    * @return: 
    * @Author: jingmi
    * @Date: 2024/7/17
    */
    
    @Override
    public Long getValidUid(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if (Objects.isNull(uid)) {
            return null;
        }
        String oldToken = RedisUtils.getStr(getUserTokenKey(uid));
        if(StringUtils.isBlank(oldToken)){
            return null;
        }
        return  Objects.equals(token,oldToken)? uid : null;

    }
    
    /** 
    * @Description: 续期
    * @Param: 
    * @return: 
    * @Author: jingmi
    * @Date: 2024/7/17
    */

    @Override
    public void renewAlTokenIfNecessary(String token) {
        Long uid = getValidUid(token);
        String userTokenKey = getUserTokenKey(uid);
        Long expireDays = RedisUtils.getExpire(userTokenKey, TimeUnit.DAYS);
        if (expireDays==-NULL_TOKEN){
            return;
        }
        if (expireDays<= NEED_UPDATE){
            RedisUtils.expire(getUserTokenKey(uid), TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
        }
        


    }
}
