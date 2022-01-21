package com.tools.service.impl;

import com.hogwarts.utils.XmlBuilder;
import com.tools.entity.City;
import com.tools.entity.CityList;
import com.tools.service.CityService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Override
    public List<City> listCity() throws Exception {
        //读取Resource目录下的XML文件
        Resource resource = new ClassPathResource("cityList.xml");
        //利用输入流获取XML文件内容
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
        br.close();
        //XML转为JAVA对象
        CityList cityList = (CityList) XmlBuilder.xmlStrToObject(CityList.class, buffer.toString());
        return cityList.getCityList();
    }
}