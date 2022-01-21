package com.dubbo.payment.service;

import com.dubbo.payment.entity.Balance;

public interface BalanceService {
 
    Balance getBalance(Integer id);
}