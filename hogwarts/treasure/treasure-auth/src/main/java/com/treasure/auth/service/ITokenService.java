package com.treasure.auth.service;

public interface ITokenService {
    public String getToken(String username, String password);
}
