package com.jingmi.chat.common.websocket.service.adapter;

import com.jingmi.chat.common.user.domain.entity.User;
import com.jingmi.chat.common.websocket.domain.vo.resp.WSBaseResp;
import com.jingmi.chat.common.websocket.domain.vo.resp.WSLoginSuccess;
import com.jingmi.chat.common.websocket.domain.vo.resp.WSLoginUrl;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import com.jingmi.chat.common.websocket.domain.enums.WSRespTypeEnum;
/**
 * @program: jingmiChat
 * @description: 转换器
 * @author: JingMi
 * @create: 2024-07-14 11:29
 **/

public class WebSocketAdapter {
    public static WSBaseResp<?> buildLoginResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> wsBaseRespWSBaseResp = new WSBaseResp<>();
        wsBaseRespWSBaseResp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        wsBaseRespWSBaseResp.setData(new WSLoginUrl(wxMpQrCodeTicket.getUrl()));
        return wsBaseRespWSBaseResp;
    }

    public static WSBaseResp<?> buildLoginSuccessResp(String token, User user) {
        WSBaseResp<WSLoginSuccess> wsBaseRespWSBaseResp = new WSBaseResp<>();
        wsBaseRespWSBaseResp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess wsLoginSuccess = WSLoginSuccess.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .uid(user.getId())
                .token(token).build();

        wsBaseRespWSBaseResp.setData(wsLoginSuccess);
        return wsBaseRespWSBaseResp;
    }

    public static WSBaseResp<?> buildWitAuthorizeResp() {
        WSBaseResp<WSLoginUrl> wsBaseRespWSBaseResp = new WSBaseResp<>();
        wsBaseRespWSBaseResp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return wsBaseRespWSBaseResp;
    }

    public static WSBaseResp<?> buildInvalidTokenResp() {
        WSBaseResp<WSLoginUrl> wsBaseRespWSBaseResp = new WSBaseResp<>();
        wsBaseRespWSBaseResp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return wsBaseRespWSBaseResp;
    }
}
