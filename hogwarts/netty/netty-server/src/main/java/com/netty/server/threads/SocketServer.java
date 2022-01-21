package com.netty.server.threads;

import com.netty.server.service.ServerService;
import com.netty.server.service.impl.NettyChannelServiceImpl;
import com.netty.server.service.impl.ServerServiceImpl;
import com.netty.server.service.impl.SocketChannelServiceImpl;
import io.netty.channel.ChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Slf4j
@Component
public class SocketServer extends Thread implements InitializingBean {

    @Autowired
    ServerService serverService;



    @Override
    public void run() {
        ServerServiceImpl serverServiceImpl = new ServerServiceImpl<>(new SocketChannelServiceImpl());
        log.info("SocketServer run {}", serverServiceImpl);
        serverServiceImpl.start(new InetSocketAddress("127.0.0.1", 8099));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
    }
}
