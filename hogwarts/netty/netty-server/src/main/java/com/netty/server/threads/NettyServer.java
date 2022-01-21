package com.netty.server.threads;

import com.netty.server.service.impl.NettyChannelServiceImpl;
import com.netty.server.service.impl.ServerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Slf4j
@Component
public class NettyServer extends Thread implements InitializingBean {

    @Override
    public void run() {
        ServerServiceImpl serverServiceImpl = new ServerServiceImpl<>(new NettyChannelServiceImpl());
        log.info("NettyServer run {}", serverServiceImpl);
        serverServiceImpl.start(new InetSocketAddress("127.0.0.1", 8088));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
    }
}
