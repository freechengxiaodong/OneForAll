package com.provider.service;

import com.provider.service.impl.TestServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "PROVIDER-TWO", fallback = TestServiceImpl.class)
public interface TestService {

    @RequestMapping(value = "/test/index", method = RequestMethod.GET)
    public String showMessage();
}
