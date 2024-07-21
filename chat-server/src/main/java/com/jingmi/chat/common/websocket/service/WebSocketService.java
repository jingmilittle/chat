package com.jingmi.chat.common.websocket.service;

import io.netty.channel.Channel;

public interface WebSocketService {
    void connect(Channel channel);

    void handleLoginReq(Channel channel);

    void offline(Channel channel);

    void ScanSuccessLogin(Integer code, Long id);

    void waitAuthorize(Integer code);
    void authroize(Channel channel,String token);
}
