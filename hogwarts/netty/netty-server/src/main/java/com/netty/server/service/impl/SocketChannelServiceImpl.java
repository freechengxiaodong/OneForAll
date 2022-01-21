package com.netty.server.service.impl;

import com.netty.server.handle.SocketServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Gjing
 * <p>
 * WebSocket服务初始化器
 **/
@Slf4j
@Service
public class SocketChannelServiceImpl extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        log.info("SocketChannelServiceImpl initChannel 初始化服务器");

        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
        socketChannel.pipeline().addLast(new HttpServerCodec());
        //以块的方式来写的处理器
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
        socketChannel.pipeline().addLast(new HttpObjectAggregator(1024 * 62));
        socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/channel", "WebSocket", true, 65536 * 10));
        socketChannel.pipeline().addLast(new SocketServerHandler());//自定义消息处理类
    }
}
