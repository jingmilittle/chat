package com.jingmi.chat.common.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;

import com.jingmi.chat.common.common.domain.enums.YesOrNoEnums;
import com.jingmi.chat.common.user.domain.entity.ItemConfig;
import com.jingmi.chat.common.user.domain.entity.User;
import com.jingmi.chat.common.user.domain.entity.UserBackpack;
import com.jingmi.chat.common.user.domain.vo.resp.BadgeResp;
import com.jingmi.chat.common.user.domain.vo.resp.UserInfoResp;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: 用户适配器
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-03-19
 */
@Slf4j
public class UserAdapter {
    public static User buildUserSave(String openId) {
        return User.builder()
                .openId(openId)
                .build();

    }

    public static User buildAuthorizeUser(Long id, WxOAuth2UserInfo userInfo) {
        User user = new User();
        user.setId(id);
        user.setAvatar(userInfo.getHeadImgUrl());
        user.setName(userInfo.getNickname());
        user.setSex(userInfo.getSex());
        if (userInfo.getNickname().length() > 6) {
            user.setName("名字过长" + RandomUtil.randomInt(100000));
        } else {
            user.setName(userInfo.getNickname());
        }
        return user;
    }

    public static UserInfoResp buildUserInfo(User user, Integer changeNameCardCount) {
        UserInfoResp userInfoResp = BeanUtil.copyProperties(user, UserInfoResp.class);
        userInfoResp.setModifyNameChance(changeNameCardCount);
        return userInfoResp;

    }

    /**
    * @Description: 组装返回数据 且根据佩戴 拥有来排序
    * @Param: itemConfigs 所有徽章 backpacks用户背包里有的徽章  user用户当前佩戴的
    * @return:
    * @Author: jingmi
    * @Date: 2024/7/23
    */
    public static List<BadgeResp> buildBadgeResp(List<ItemConfig> itemConfigs, List<UserBackpack> backpacks, User user) {
        Set<Long> obtainItemSet = backpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());
        return  itemConfigs.stream().map(itemConfig -> {
            BadgeResp badgeResp = new BadgeResp();
            BeanUtil.copyProperties(itemConfig, badgeResp);
            badgeResp.setObtain(obtainItemSet.contains(itemConfig.getId()) ? YesOrNoEnums.YES.getStatus() : YesOrNoEnums.NO.getStatus());
            badgeResp.setWearing(Objects.equals(itemConfig.getId(), user.getItemId()) ? YesOrNoEnums.YES.getStatus() : YesOrNoEnums.NO.getStatus());
            return badgeResp;
        }).sorted(Comparator.comparing(BadgeResp::getWearing, Comparator.reverseOrder()).thenComparing(BadgeResp::getObtain, Comparator.reverseOrder())).collect(Collectors.toList());

    }

//    public static User buildUser(String openId) {
//        User user = new User();
//        user.setOpenId(openId);
//        return user;
//    }
//
//    public static User buildAuthorizeUser(Long id, WxOAuth2UserInfo userInfo) {
//        User user = new User();
//        user.setId(id);
//        user.setAvatar(userInfo.getHeadImgUrl());
//        user.setName(userInfo.getNickname());
//        user.setSex(userInfo.getSex());
//        if (userInfo.getNickname().length() > 6) {
//            user.setName("名字过长" + RandomUtil.randomInt(100000));
//        } else {
//            user.setName(userInfo.getNickname());
//        }
//        return user;
//    }
//
//    public static UserInfoResp buildUserInfoResp(User userInfo, Integer countByValidItemId) {
//        UserInfoResp userInfoResp = new UserInfoResp();
//        BeanUtil.copyProperties(userInfo, userInfoResp);
//        userInfoResp.setModifyNameChance(countByValidItemId);
//        return userInfoResp;
//    }
//
//    public static List<BadgeResp> buildBadgeResp(List<ItemConfig> itemConfigs, List<UserBackpack> backpacks, User user) {
//        if (ObjectUtil.isNull(user)) {
//            // 这里 user 入参可能为空，防止 NPE 问题
//            return Collections.emptyList();
//        }
//
//        Set<Long> obtainItemSet = backpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());
//        return itemConfigs.stream().map(a -> {
//            BadgeResp resp = new BadgeResp();
//            BeanUtil.copyProperties(a, resp);
//            resp.setObtain(obtainItemSet.contains(a.getId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
//            resp.setWearing(ObjectUtil.equal(a.getId(), user.getItemId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
//            return resp;
//        }).sorted(Comparator.comparing(BadgeResp::getWearing, Comparator.reverseOrder())
//                .thenComparing(BadgeResp::getObtain, Comparator.reverseOrder()))
//                .collect(Collectors.toList());
//    }
}
