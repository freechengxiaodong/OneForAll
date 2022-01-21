package com.dubbo.payment.service;

import com.dubbo.account.entity.Balance;
import org.springframework.web.bind.annotation.RequestParam;

public interface BalanceService {
    Balance getBalance(@RequestParam("id") Integer id);
}