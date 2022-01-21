package com.netty.server.controller;

import com.netty.server.annotation.CheckToken;
import com.netty.server.annotation.PassToken;
import com.netty.server.service.TokenService;
import com.netty.server.utils.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    TokenService tokenService;


    @PostMapping("login")
    public Map login(@RequestParam("username") String username, @RequestParam("password") String password) throws UnsupportedEncodingException {

        //加解密算法
        String encodePassword = Base64.getEncoder().encodeToString("123456".getBytes("UTF-8"));
        log.info("Base64加密后密码：{}", encodePassword);
        String aesEncodePassword = AesUtil.encrypt(encodePassword, "123456");
        log.info("加密后密码：{}", aesEncodePassword);
        String passwordDecrypt = AesUtil.decrypt(aesEncodePassword, "123456").trim();
        log.info("解密后密码：{}", passwordDecrypt);
        log.info("Base64解密后密码：{}", new String(Base64.getDecoder().decode(passwordDecrypt)));

        //登陆成功
        Map<String, Object> map = new HashMap<>();
        String token = tokenService.getToken(username, password);
        log.info("登陆成功,token:{}",token);
        map.put("token", token);

        return map;
    }

    @CheckToken
    @PostMapping("show")
    public String show() {

        return "show info";
    }
}
