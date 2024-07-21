package com.jingmi.chat.common.user.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.generator.config.IFileCreate;
import com.jingmi.chat.common.common.exception.BusinessException;
import com.jingmi.chat.common.user.domain.entity.User;
import com.jingmi.chat.common.user.domain.entity.vo.resp.UserInfoResp;
import com.jingmi.chat.common.user.domain.enums.ItemEnum;
import com.jingmi.chat.common.user.domain.vo.ModifyNameReq;
import com.jingmi.chat.common.user.mapper.UserMapper;
import com.jingmi.chat.common.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jingmi.chat.common.user.service.adapter.UserAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
    private UserBackpackDao userBackpackDao;
    public User getByOpenId(String openId) {
       return lambdaQuery().eq(User::getOpenId, openId).one();
    }
    @Transactional
    @Override
    public Long register(User user) {
         this.save(user);
        //用户注册事件
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
    public void modifyName(String name, Long uid) {
        User oldUser = getUserByName(name);
        if (Objects.nonNull(oldUser)){
            throw new BusinessException("用户名已存在");
        }


    }

    private  User getUserByName(String name) {
        return lambdaQuery().eq(User::getName, name).one();

    }
}
