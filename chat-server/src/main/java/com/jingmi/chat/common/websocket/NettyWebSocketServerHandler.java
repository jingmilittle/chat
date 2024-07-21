package com.jingmi.chat.common.websocket;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.google.protobuf.DescriptorProtos;
import com.jingmi.chat.common.websocket.domain.enums.WSReqTypeEnum;
import com.jingmi.chat.common.websocket.domain.vo.req.WSBaseReq;
import com.jingmi.chat.common.websocket.service.NettyUtil;
import com.jingmi.chat.common.websocket.service.WebSocketService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import static com.jingmi.chat.common.websocket.domain.enums.WSReqTypeEnum.*;


@Slf4j
@Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private WebSocketService webSocketService;
    //管道激活时保存
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        webSocketService = SpringUtil.getBean(WebSocketService.class);
        log.info("客户端连接");
        log.info("webSocketService{}",webSocketService);
        webSocketService.connect(ctx.channel());
    }

    //客户端主动下线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
       userOffline(ctx.channel());
    }


    //事件触发 例如 握手成功 用户下线
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

       if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
           log.info("握手完成");
           String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
           if (StrUtil.isNotBlank(token)){
               webSocketService.authroize( ctx.channel(), token);
           }

       }else if (evt instanceof IdleStateEvent) {
           IdleStateEvent event =  (IdleStateEvent) evt;
        if (event.state() == IdleState.READER_IDLE){
            log.info("读空闲");
            //todo 用户下线
//            ctx.channel().close();
            userOffline( ctx.channel());
        }
       }
    }
    private  void userOffline(Channel channel){
        webSocketService.offline(channel);
        channel.close();
    }
    /**
    * websocket 接受到消息就会走这里 读取管道消息
     **/
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())){
            case AUTHORIZE:
                webSocketService.authroize( channelHandlerContext.channel(), wsBaseReq.getData());
                break;
            case HEARTBEAT:
                break;
            case LOGIN:
                //鉴权
                log.info("请求二维码");
                webSocketService.handleLoginReq(channelHandlerContext.channel());

        }
    }
}
