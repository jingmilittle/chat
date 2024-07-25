package com.jingmi.chat.common.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jingmi.chat.common.common.domain.enums.YesOrNoEnums;
import com.jingmi.chat.common.common.utils.AssertUtil;
import com.jingmi.chat.common.user.dao.UserBackpackDao;
import com.jingmi.chat.common.user.domain.entity.UserBackpack;
import com.jingmi.chat.common.user.domain.enums.IdempotentEnum;
import com.jingmi.chat.common.user.service.IUserBackpackService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * @program: jingmiChat
 * @description:
 * @author: JingMi
 * @create: 2024-07-24 22:50
 **/
@Service
public class IUserBackpackServiceImpl implements IUserBackpackService {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private UserBackpackDao userBackpackDao;
    /**
     * @Description: 用户发放物品
     * @Param: userId 用户id itemId 物品id idempotentEnum 幂等性枚举 businessId 业务id
     * @return:
     * @Author: jingmi
     * @Date: 2024/7/24
     */
    @Override
    public void acquireItem(Long userId, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        String idempotent = getIdempotent(itemId, idempotentEnum, businessId);
        RLock lock = redissonClient.getLock("acquireItem" + idempotent);
        boolean b = lock.tryLock();
        AssertUtil.isTrue(b,"请求太频繁了");
        try {
            UserBackpack byIdempotent = userBackpackDao.getByIdempotent(idempotent);
            if (Objects.nonNull(byIdempotent)){
                return;
            }
            //业务检查
            //发放物品
            UserBackpack insert = UserBackpack.builder()
                    .uid(userId)
                    .itemId(itemId)
                    .status(YesOrNoEnums.NO.getStatus())
                    .idempotent(idempotent)
                    .build();
            userBackpackDao.save(insert);
        } finally {
            lock.unlock();
        }
    }

    /**
    * 生成幂等号
     */

    private String  getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%S",itemId,idempotentEnum.getType(),businessId);
    }

    @Override
    public boolean saveBatch(Collection<UserBackpack> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<UserBackpack> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<UserBackpack> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(UserBackpack entity) {
        return false;
    }

    @Override
    public UserBackpack getOne(Wrapper<UserBackpack> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<UserBackpack> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<UserBackpack> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<UserBackpack> getBaseMapper() {
        return null;
    }
}
