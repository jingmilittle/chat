package com.jingmi.chat.common.websocket;

import cn.hutool.core.net.url.UrlBuilder;
import com.jingmi.chat.common.websocket.service.NettyUtil;
import io.netty.channel.*;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Optional;

/**
 * @program: jingmiChat
 * @description: 我自己的握手处理器
 * @author: JingMi
 * @create: 2024-07-20 11:56
 **/

public class MyHeadHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //第一次握手
        final HttpObject httpObject = (HttpObject) msg;
        if (httpObject instanceof HttpRequest) {
            final HttpRequest req = (HttpRequest) httpObject;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(req.getUri());
//            CharSequence token = urlBuilder.getQuery().get("token");

            Optional<String> tokenOptional = Optional.of(urlBuilder).map(UrlBuilder::getQuery).map(k -> k.get("token")).map(CharSequence::toString);
            tokenOptional.ifPresent(s -> NettyUtil.setAttr(ctx.channel(),NettyUtil.TOKEN, s));
            req.setUri(urlBuilder.getPath().toString());

        }
            ctx.fireChannelRead(msg);
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        //第一次握手
//        final HttpObject httpObject = (HttpObject) msg;
//        if (httpObject instanceof HttpRequest) {
//            final HttpRequest req = (HttpRequest) httpObject;
//            //取token
//            String token = req.headers().get("Sec-WebSocket-Protocol");
//            //讲附件放进从channel的通道里
//            Attribute<Object> token1 = ctx.channel().attr(AttributeKey.valueOf("token"));
//            token1.set(token);
//            //设置回请求头
//            final WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
//                    req.getUri(),
//                    token, false);
//            final WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
//            if (handshaker == null) {
//                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
//            } else {
//                //第一次之后就不需要了直接移除
//                ctx.pipeline().remove(this);
//                final ChannelFuture handshakeFuture = handshaker.handshake(ctx.channel(), req);
//                //发送回应
//                handshakeFuture.addListener(new ChannelFutureListener() {
//                    @Override
//                    public void operationComplete(ChannelFuture future) {
//                        if (!future.isSuccess()) {
//                            ctx.fireExceptionCaught(future.cause());
//                        } else {
//                            // Kept for compatibility
//                            ctx.fireUserEventTriggered(
//                                    WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
//                        }
//                    }
//                });
//            }
//        } else
//            ctx.fireChannelRead(msg);
//    }
}
