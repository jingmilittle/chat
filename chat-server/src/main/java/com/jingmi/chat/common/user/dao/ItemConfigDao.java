package com.jingmi.chat.common.user.dao;

import com.jingmi.chat.common.user.domain.entity.ItemConfig;
import com.jingmi.chat.common.user.mapper.ItemConfigMapper;
import com.jingmi.chat.common.user.service.IItemConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 功能物品配置表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/jingmilittle">jingmi</a>
 * @since 2024-07-20
 */
@Service
public class ItemConfigDao extends ServiceImpl<ItemConfigMapper, ItemConfig>   implements IItemConfigService  {


    @Override
    public List<ItemConfig> getValidByType(Integer itemType) {
      return   lambdaQuery()
                .eq(ItemConfig::getType,itemType)
                .list();

    }
}
