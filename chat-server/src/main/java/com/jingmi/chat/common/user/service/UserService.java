package com.jingmi.chat.common.user.service;

import com.jingmi.chat.common.user.domain.entity.User;
import com.jingmi.chat.common.user.domain.vo.resp.BadgeResp;
import com.jingmi.chat.common.user.domain.vo.resp.UserInfoResp;

import java.util.List;

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

    List<BadgeResp> badges(Long uid);

    void wearingBadge(Long uid,Long badgeId);
}
