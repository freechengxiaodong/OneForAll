package com.dubbo.account.controller;

import com.dubbo.account.entity.User;
import com.dubbo.payment.service.BalanceService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {

    @Reference(group = "balance")
    BalanceService balanceService;

    final static Map<Integer, User> userMap = new HashMap() {
        {
            put(1, new User(1, "张三"));
            put(2, new User(2, "李四"));
            put(3, new User(3, "王五"));
        }
    };

    @RequestMapping("/acc/user")
    public User getUser(@RequestParam Integer id) {

        if (id != null && userMap.containsKey(id)) {
            User user = userMap.get(id);
            user.setBalance(balanceService.getBalance(user.getId()));
            return user;
        }

        return new User(0, "");
    }
}