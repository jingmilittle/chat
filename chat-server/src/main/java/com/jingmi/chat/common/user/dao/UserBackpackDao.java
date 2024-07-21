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

    public Integer getCountByValidItemId(Long uid, Long itemId) {
        return lambdaQuery()
                .eq(UserBackpack::getUid,uid)
                .eq(UserBackpack::getItemId,itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnums.NO.getStatus())
                .count();
    }
}
