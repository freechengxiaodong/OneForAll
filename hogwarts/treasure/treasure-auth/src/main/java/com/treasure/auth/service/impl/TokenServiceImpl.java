package com.treasure.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.treasure.auth.service.ITokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements ITokenService {
    @Override
    public String getToken(String username, String password) {

        return JWT.create().withAudience(username).sign(Algorithm.HMAC256(password));
    }
}
