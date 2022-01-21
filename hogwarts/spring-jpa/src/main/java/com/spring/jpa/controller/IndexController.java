package com.spring.jpa.controller;

import com.spring.jpa.dao.NewsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    NewsDao newsDao;

    @RequestMapping("/show")
    public String show(){

        return newsDao.getOne(1).getIntro();
    }
}
