package com.jingmi.chat.common.user.dao;

import com.jingmi.chat.common.common.domain.enums.YesOrNoEnums;
import com.jingmi.chat.common.user.domain.entity.UserBackpack;
import com.jingmi.chat.common.user.mapper.UserBackpackMapper;
import com.jingmi.chat.common.user.service.IUserBackpackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户背包表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/jingmilittle">jingmi</a>
 * @since 2024-07-20
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack>   {
   //获取可使用的改名次数
    public Integer getCountByValidItemId(Long uid, Long itemId) {
        return lambdaQuery()
                .eq(UserBackpack::getUid,uid)
                .eq(UserBackpack::getItemId,itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnums.NO.getStatus())
                .count();
    }
    //获取改名卡
    public UserBackpack getFirstValidItemByItemId(Long uid, Long itemId) {
       return   lambdaQuery()
                .eq(UserBackpack::getUid,uid)
                .eq(UserBackpack::getItemId,itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnums.NO.getStatus())
                .orderByAsc(UserBackpack::getId)
                .last("limit 1")
                .one();
    }

    public boolean userItem(UserBackpack changeNameCard) {
      return   lambdaUpdate()
                .eq(UserBackpack::getId,changeNameCard.getId())
                .eq(UserBackpack::getStatus, YesOrNoEnums.NO.getStatus())
                .set(UserBackpack::getStatus, YesOrNoEnums.YES.getStatus())
                .update();


    }
}
