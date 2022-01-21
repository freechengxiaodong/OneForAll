package com.netty.server.service.impl;

import com.netty.server.handle.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Gjing
 * <p>
 * netty服务初始化器
 **/
@Slf4j
@Service
public class NettyChannelServiceImpl extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        log.info("NettyChannelServiceImpl initChannel 初始化服务器");

        //添加编解码
        socketChannel.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
        socketChannel.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));

        //添加自定义的助手类
        socketChannel.pipeline().addLast(new NettyServerHandler());
    }
}
