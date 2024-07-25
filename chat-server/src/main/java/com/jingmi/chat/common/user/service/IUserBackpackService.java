package com.jingmi.chat.common.user.service;

import com.jingmi.chat.common.user.domain.entity.UserBackpack;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jingmi.chat.common.user.domain.enums.IdempotentEnum;

/**
 * <p>
 * 用户背包表 服务类
 * </p>
 *
 * @author <a href="https://github.com/jingmilittle">jingmi</a>
 * @since 2024-07-20
 */
public interface IUserBackpackService extends IService<UserBackpack> {

    /**
    * @Description: 用户发放物品
    * @Param: userId 用户id itemId 物品id idempotentEnum 幂等性枚举 businessId 业务id
    * @return:
    * @Author: jingmi
    * @Date: 2024/7/24
    */


    void acquireItem(Long userId, Long itemId, IdempotentEnum idempotentEnum,String businessId);

}
