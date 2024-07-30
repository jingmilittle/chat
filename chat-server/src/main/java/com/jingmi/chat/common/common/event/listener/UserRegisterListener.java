package com.jingmi.chat.common.common.event.listener;

import com.jingmi.chat.common.common.event.UserRegisterEvent;
import com.jingmi.chat.common.user.dao.UserDao;
import com.jingmi.chat.common.user.domain.entity.User;
import com.jingmi.chat.common.user.domain.enums.IdempotentEnum;
import com.jingmi.chat.common.user.domain.enums.ItemEnum;
import com.jingmi.chat.common.user.domain.enums.ItemTypeEnum;
import com.jingmi.chat.common.user.service.IUserBackpackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @program: jingmiChat
 * @description:
 * @author: JingMi
 * @create: 2024-07-30 23:24
 **/
@Component
public class UserRegisterListener {
    @Autowired
    private IUserBackpackService userBackpackService;
    @Autowired
    private UserDao userDao;
    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class,phase = TransactionPhase.AFTER_COMMIT)
    public void  sendCard(UserRegisterEvent event) {
        User user = event.getUser();
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID,user.getId().toString());
    }
    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class,phase = TransactionPhase.AFTER_COMMIT)
    public void  sendBadge(UserRegisterEvent event) {
        User user = event.getUser();
        int registerUserCount = userDao.count();
        if (registerUserCount<=10){
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP10_BADGE.getId(), IdempotentEnum.UID,user.getId().toString());
        } else if (registerUserCount<=100) {
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID,user.getId().toString());
        }


    }
}
