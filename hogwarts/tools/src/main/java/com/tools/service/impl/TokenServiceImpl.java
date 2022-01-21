package com.tools.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tools.service.TokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public String getToken(String username, String password) {

        return JWT.create().withAudience(username)
                .sign(Algorithm.HMAC256(password));
    }
}
