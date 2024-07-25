package com.jingmi.chat.common.user.service.cache;

import com.jingmi.chat.common.user.dao.ItemConfigDao;
import com.jingmi.chat.common.user.domain.entity.ItemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: jingmiChat
 * @description: 获取或存放对应类的缓存
 * @author: JingMi
 * @create: 2024-07-22 22:33
 **/
@Component
public class ItemCache {
    @Autowired
    private ItemConfigDao itemConfigDao;
    //获取缓存徽章
    @Cacheable(cacheNames = "item",key="'itemByType:'+#ItemType")
    public List<ItemConfig> getByType(Integer ItemType){

        return itemConfigDao.getValidByType(ItemType);
    }
    //清除操作
    @CacheEvict(cacheNames = "item",key="'itemByType:'+#ItemType")
    public void evict(Integer ItemType){}

}
