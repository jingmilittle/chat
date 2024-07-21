package com.jingmi.chat.common.websocket.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jingmi.chat.common.user.dao.UserDao;
import com.jingmi.chat.common.user.domain.entity.User;
import com.jingmi.chat.common.user.service.LoginService;
import com.jingmi.chat.common.websocket.domain.dto.WSChannelExtraDTO;
import com.jingmi.chat.common.websocket.domain.vo.resp.WSBaseResp;
import com.jingmi.chat.common.websocket.service.WebSocketService;
import com.jingmi.chat.common.websocket.service.adapter.WebSocketAdapter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: jingmiChat
 * @description: websocket 逻辑处理
 * @author: JingMi
 * @create: 2024-07-14 10:16
 **/
@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Autowired
    @Lazy
    private WxMpService wxMpService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginService loginService;

    //管理所有在线用户的连接(登录态,游客)
    private static  final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_CHANNEL_MAP = new ConcurrentHashMap<>();
    //过期时间
    public static final Duration DURATION  = Duration.ofHours(1);
    //最大容量
    public static final int MAXIMUM_SIZE = 1000;
    //登录验证map
    private static  final Cache<Integer,Channel > WAIT_LOGIN_CHANNEL_MAP = Caffeine.newBuilder().maximumSize(MAXIMUM_SIZE).expireAfterWrite(DURATION).build();
    @Override
    public void connect(Channel channel) {
        ONLINE_CHANNEL_MAP.put(channel, new WSChannelExtraDTO());
    }

    @SneakyThrows
    @Override
    public void handleLoginReq(Channel channel) {
        //随机码
        Integer code = generateLoginCode(channel);
        //生成带参二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) DURATION.getSeconds());
        //发送二维码
        sendMsg(channel, WebSocketAdapter.buildLoginResp(wxMpQrCodeTicket));
    }

    @Override
    public void offline(Channel channel) {
        ONLINE_CHANNEL_MAP.remove(channel);
        //todo 用户下线
    }

    @Override
    public void ScanSuccessLogin(Integer code, Long uid) {
        Channel channel = WAIT_LOGIN_CHANNEL_MAP.getIfPresent(code);
        if (Objects.isNull(channel)){
            return;
        }
        User user = userDao.getById(uid);

        //移除
        WAIT_LOGIN_CHANNEL_MAP.invalidate(code);
        //生成用户token
        String token = loginService.login(uid);
        sendMsg(channel, WebSocketAdapter.buildLoginSuccessResp(token,user));

    }

    @Override
    public void waitAuthorize(Integer code) {
        Channel channel = WAIT_LOGIN_CHANNEL_MAP.getIfPresent(code);
        if (Objects.isNull(channel)){
            return;
        }
        sendMsg(channel, WebSocketAdapter.buildWitAuthorizeResp());
    }

    @Override
    public void authroize(Channel channel, String token) {
        //判断是否有效
        Long validUid = loginService.getValidUid(token);
        if(Objects.nonNull(validUid)){
            //没过期
            User user = userDao.getById(validUid);
            loginSuccess(channel, token, user) ;

        }else {
            //过期
            sendMsg(channel, WebSocketAdapter.buildInvalidTokenResp());
        }
    }

    private void  loginSuccess(Channel channel, String token, User user) {
        //保存channel uid
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_CHANNEL_MAP.get(channel);
        wsChannelExtraDTO.setUid(user.getId());
        //todo 用户状态变更
        sendMsg(channel, WebSocketAdapter.buildLoginSuccessResp(token,user));
    }

    private void sendMsg(Channel channel, WSBaseResp<?> wsBaseRespWSBaseResp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsBaseRespWSBaseResp)));

    }

    //生成保存code 与 channel
    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do {
             code = RandomUtil.randomInt(Integer.MAX_VALUE);
        }while (Objects.nonNull(WAIT_LOGIN_CHANNEL_MAP.asMap().putIfAbsent(code,channel)));
        return code;
    }
}
