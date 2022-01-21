package com.provider.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping("index")
    public String index(){
        log.info("provider two test index ");
        return "provider two test index";
    }
}

