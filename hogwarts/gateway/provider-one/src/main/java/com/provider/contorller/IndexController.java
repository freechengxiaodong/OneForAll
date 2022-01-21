package com.provider.contorller;

import com.provider.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    TestService testService;

    @RequestMapping("index")
    public String index(){

        log.info("provider one index index " + testService.showMessage());
        return "provider one index index request:{"+testService.showMessage()+"}";
    }
}
