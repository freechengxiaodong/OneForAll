package com.payment.service;

import com.payment.entity.City;

import java.util.List;

/**
 * 城市数据接口
 */
public interface CityService {

    List<City> listCity() throws Exception;
}