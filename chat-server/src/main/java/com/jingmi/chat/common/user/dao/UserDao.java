package com.jingmi.chat.common.user.dao;

import com.jingmi.chat.common.common.event.UserRegisterEvent;
import com.jingmi.chat.common.common.utils.AssertUtil;
import com.jingmi.chat.common.user.domain.entity.ItemConfig;
import com.jingmi.chat.common.user.domain.entity.User;
import com.jingmi.chat.common.user.domain.entity.UserBackpack;
import com.jingmi.chat.common.user.domain.enums.ItemTypeEnum;
import com.jingmi.chat.common.user.domain.vo.resp.BadgeResp;
import com.jingmi.chat.common.user.domain.vo.resp.UserInfoResp;
import com.jingmi.chat.common.user.domain.enums.ItemEnum;
import com.jingmi.chat.common.user.mapper.UserMapper;
import com.jingmi.chat.common.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jingmi.chat.common.user.service.adapter.UserAdapter;
import com.jingmi.chat.common.user.service.cache.ItemCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/jingmilittle">jingmi</a>
 * @since 2024-07-13
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private UserBackpackDao userBackpackDao;
    @Autowired
    private ItemConfigDao itemConfigDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    public User getByOpenId(String openId) {
       return lambdaQuery().eq(User::getOpenId, openId).one();
    }
    @Transactional
    @Override
    public Long register(User user) {
         this.save(user);
        // todo 用户注册事件
        applicationEventPublisher.publishEvent(new UserRegisterEvent( this,user));
     return  user.getId();

    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = this.getById(uid);
        Integer changeNameCardCount = userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        UserInfoResp userInfoResp = UserAdapter.buildUserInfo(user,changeNameCardCount);
        return userInfoResp;
    }
    /**
    *改名字
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyName(String name, Long uid) {
        User oldUser = getUserByName(name);
        AssertUtil.isEmpty(oldUser,"名字已存在,请换一个");
        UserBackpack changeNameCard = userBackpackDao.getFirstValidItemByItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(changeNameCard,"改名卡不足");
        boolean isSuccess = userBackpackDao.userItem(changeNameCard);
        if (isSuccess){
            this.changeName(name,uid);
        }


    }


    @Override
    public List<BadgeResp> badges(Long uid) {
        //所有徽章
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        //查询用户所持有徽章
        List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uid, itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList()));
        //当前佩戴的
        User user  = this.getById(uid);
        return UserAdapter.buildBadgeResp(itemConfigs,backpacks,user);


    }
    /**
    * 切换徽章
     */

    @Override
    public void wearingBadge(Long uid,Long itemId) {
        UserBackpack firstValidItemByItemId = userBackpackDao.getFirstValidItemByItemId(uid, itemId);
        AssertUtil.isNotEmpty(firstValidItemByItemId,"该徽章不存在");
        ItemConfig itemConfig = itemConfigDao.getById(firstValidItemByItemId.getItemId());
        AssertUtil.equal(itemConfig.getType(),ItemTypeEnum.BADGE.getType(),"该物品不是徽章");
        this.changeBadge( uid, itemId);
    }

    private void changeBadge(Long uid, Long itemId) {
        lambdaUpdate()
                .eq(User::getId,uid)
                .set(User::getItemId,itemId)
                .update();
    }

    private boolean changeName(String name,Long uid){
       return lambdaUpdate()
                .eq(User::getId,uid)
                .set(User::getName,name)
                .update();
       //todo 删除缓存
    }

    private  User getUserByName(String name) {
        return lambdaQuery().eq(User::getName, name).one();

    }
}
