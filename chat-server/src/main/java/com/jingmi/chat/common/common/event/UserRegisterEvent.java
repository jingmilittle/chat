package com.jingmi.chat.common.common.event;

import com.jingmi.chat.common.user.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @program: jingmiChat
 * @description: 用户注册事件
 * @author: JingMi
 * @create: 2024-07-30 23:15
 **/
@Getter
@Setter
public class UserRegisterEvent extends ApplicationEvent {
    private User user;
    public UserRegisterEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
