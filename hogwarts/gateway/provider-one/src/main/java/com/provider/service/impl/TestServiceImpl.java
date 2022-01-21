package com.provider.service.impl;

import com.provider.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public String showMessage() {
        return "provider one TestServiceImpl showMessage";
    }
}
