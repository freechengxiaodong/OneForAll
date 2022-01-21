package com.netty.server.service;

public interface TokenService {

    public String getToken(String username, String password);
}
