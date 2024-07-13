package com.jingmi.chat.common.websocket;


import cn.hutool.json.JSONUtil;
import com.google.protobuf.DescriptorProtos;
import com.jingmi.chat.common.websocket.domain.enums.WSReqTypeEnum;
import com.jingmi.chat.common.websocket.domain.vo.req.WSBaseReq;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import static com.jingmi.chat.common.websocket.domain.enums.WSReqTypeEnum.*;


@Slf4j
@Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //事件触发
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

       if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
           log.info("握手完成");
       }else if (evt instanceof IdleStateEvent) {
           IdleStateEvent event =  (IdleStateEvent) evt;
        if (event.state() == IdleState.READER_IDLE){
            log.info("读空闲");
            //todo 用户下线

        }
       }
    }
    /**
    * websocket 接受到消息就会走这里
     **/
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())){
            case AUTHORIZE:
                break;
            case HEARTBEAT:
                break;
            case LOGIN:
                //鉴权
                log.info("请求二维码");
                channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("123"));

        }
    }
}
