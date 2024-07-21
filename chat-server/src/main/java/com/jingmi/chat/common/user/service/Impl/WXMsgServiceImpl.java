package com.jingmi.chat.common.user.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.jingmi.chat.common.user.dao.UserDao;
import com.jingmi.chat.common.user.domain.entity.User;
import com.jingmi.chat.common.user.service.UserService;
import com.jingmi.chat.common.user.service.WXMsgService;
import com.jingmi.chat.common.user.service.adapter.TextBuilder;
import com.jingmi.chat.common.user.service.adapter.UserAdapter;
import com.jingmi.chat.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: jingmiChat
 * @description: 微信消息服务
 * @author: JingMi
 * @create: 2024-07-13 20:34
 **/
@Service
@Slf4j
public class WXMsgServiceImpl  implements WXMsgService {
    @Autowired
    private  WebSocketService webSocketService;
    /**
    * OPEN_ID
     * LOGIN_CODE
     */
    private static final ConcurrentHashMap<String,Integer> Wait_AUTHORIZE_MAP = new ConcurrentHashMap<>();


    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    public static final String GRSCENE_ = "grscene_";
    @Value("${wx.mp.callback}")
    private String callback;
    @Autowired
    private UserDao userDao;
    /**
    *返回一个微信授权链接
     */
    @Override
    public WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage){
        //获取事件
        Integer code = getEventKey(wxMpXmlMessage);
        if (Objects.isNull(code)){
            //没有就直接返回
            return null;
        }
        //用户注册
        String openId = wxMpXmlMessage.getFromUser();
        User user = userDao.getByOpenId(openId);
        boolean isRegistered = Objects.nonNull(user);
        boolean isAuthorized = isRegistered&&StrUtil.isNotBlank(user.getAvatar());
        //已经注册的用户
        if (isRegistered&&isAuthorized){
        //走登录成功的流程
            webSocketService.ScanSuccessLogin(code,user.getId());
        }
        //未注册
        if (!isRegistered){
            User insert = UserAdapter.buildUserSave(openId);
            //注册
            userDao.register(insert);
        }
        Wait_AUTHORIZE_MAP.put(openId,code);
        webSocketService.waitAuthorize(code);
        //UNCIODE 转码
        String authorizeUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback + "/wx/portal/public/callBack"));
        log.info("authorize:{}",authorizeUrl);
        return  new TextBuilder().build("请点击链接授权：<a href=\"" + authorizeUrl + "\">登录</a>", wxMpXmlMessage, wxMpService);
    }
    //用户授权
    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openid = userInfo.getOpenid();
        User user = userDao.getByOpenId(openid);
        if (StrUtil.isBlank(user.getAvatar())){
            fillUserInfo(user.getId(),userInfo);
        }
        Integer code = Wait_AUTHORIZE_MAP.remove(openid);
        webSocketService.ScanSuccessLogin(code,user.getId());

    }

    private void fillUserInfo(Long id ,WxOAuth2UserInfo userInfo) {
        User user = UserAdapter.buildAuthorizeUser(id, userInfo);

        try {
            userDao.updateById(user);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException(e);
        }
    }

    //判断事件
    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage){

        try {
            String eventKey = wxMpXmlMessage.getEventKey();
            String  code = eventKey.replace(GRSCENE_, "");
            return Integer.valueOf(code);
        } catch (Exception e) {
            log.error("getEventKey error:{}",e);
            return null;
        }

    }
}
