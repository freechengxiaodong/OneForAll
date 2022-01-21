package com.account.service.impl;

import com.account.entity.Balance;
import com.account.service.BalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceServiceImpl implements BalanceService {
    @Override
    public Balance getBalance(Integer id) {

        log.info("降级666");
        return new Balance(0, 0, 0, "降级666");
    }
}
