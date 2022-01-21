package com.treasure.auth.controller;

import com.treasure.auth.annotation.CheckToken;
import com.treasure.auth.annotation.PassToken;
import com.treasure.auth.service.ITokenService;
import com.treasure.auth.utils.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@CheckToken
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    ITokenService iTokenService;

    @PassToken
    @RequestMapping("login")
    public Map login(@RequestParam("username") String username, @RequestParam("password") String password) {

        log.info("Base64加密前密码：{}", password);
        //加解密算法
        String encodePassword = null;
        try {
            encodePassword = Base64.getEncoder().encodeToString(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("Base64加密后密码：{}", encodePassword);
        String aesEncodePassword = AesUtil.encrypt(encodePassword, password);
        log.info("加密后密码：{}", aesEncodePassword);
        String passwordDecrypt = AesUtil.decrypt(aesEncodePassword, password).trim();
        log.info("解密后密码：{}", passwordDecrypt);
        log.info("Base64解密后密码：{}", new String(Base64.getDecoder().decode(passwordDecrypt)));

        StringBuilder sb = new StringBuilder();
        String token = iTokenService.getToken(username, password);
        sb.append(username).append("--").append(password).append("--").append(token);

        Map map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("token", token);
        map.put("sb", sb);

        return map;
    }

    @RequestMapping("show")
    public String show() {

        return "IndexController show";
    }
}
