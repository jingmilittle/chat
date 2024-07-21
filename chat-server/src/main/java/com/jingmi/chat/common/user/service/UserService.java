package com.jingmi.chat.common.user.service;

import com.jingmi.chat.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jingmi.chat.common.user.domain.entity.vo.resp.UserInfoResp;
import com.jingmi.chat.common.user.domain.vo.ModifyNameReq;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/jingmilittle">jingmi</a>
 * @since 2024-07-13
 */
public interface UserService  {

    Long register(User user);


    UserInfoResp getUserInfo(Long uid);

    void modifyName(String name, Long uid);
}
