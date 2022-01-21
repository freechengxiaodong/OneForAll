package com.tools.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hogwarts.dto.ResDTO;
import com.hogwarts.dto.Response;
import com.hogwarts.http.DefaultHttpRequest;
import com.hogwarts.http.HttpClient;
import com.hogwarts.http.HttpRequest;
import com.hogwarts.http.HttpResponse;
import com.hogwarts.utils.JsonUtil;
import com.tools.config.properties.DemoFour;
import com.tools.config.properties.DemoOne;
import com.tools.config.properties.DemoThree;
import com.tools.config.properties.DemoTwo;
import com.tools.entity.Balance;
import com.tools.entity.City;
import com.tools.entity.Student;
import com.tools.enums.ResultCode;
import com.tools.event.TestEvent;
import com.tools.service.CityService;
import com.tools.utils.JRedisUtil;
import com.tools.utils.RedisUtil;
import com.tools.vo.BalanceVo;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class IndexController {

    //方式三 简单获取属性的方式
    @Value("${demo.name}")
    private String name;
    @Value("${demo.age}")
    private Integer age;
    @Autowired
    DemoOne demoOne;
    @Autowired
    DemoTwo demoTwo;
    @Autowired
    DemoThree demoThree;
    @Autowired
    DemoFour demoFour;
    @Autowired
    Environment env;

    //事件发布
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    //XML解析
    @Autowired
    CityService cityService;
    /*po dto vo之间快速转换*/
    @Autowired
    Mapper dozerMapper;
    @Autowired
    JRedisUtil redisUtil;
    /*引入HttpClient*/
    @Resource(name = "DefaultHttpClient")
    protected HttpClient httpClient;

    final static Map<Integer, Balance> balanceMap = new HashMap() {
        {
            put(1, new Balance(1, 10, 1000));
            put(2, new Balance(2, 0, 10000));
            put(3, new Balance(3, 100, 0));
        }
    };

    /**
     * HTTP模拟请求接口数据
     */
    @GetMapping("http")
    public com.hogwarts.dto.Response http() {

        //初始化请求对象
        HttpRequest httpRequest = new DefaultHttpRequest();
        //设置HEADER头
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("x-group", "hjiadian");
        httpRequest.setHeaderMap(headerMap);
        //请求方式 只能选择一种
        /*httpRequest.setMimeType("application/json");
        httpRequest.setMimeType("application/x-www-form-urlencoded");
        httpRequest.setMimeType("multipart/form-data");*/

        //示例-新增 POST请求数据
        /*httpRequest.setMethod("POST");
        httpRequest.setURL("http://test.wmfs.com:8008/wm/dir");
        httpRequest.getParamObjectMap().put("id", "0");
        httpRequest.getParamObjectMap().put("pid", "1");
        httpRequest.getParamObjectMap().put("name", "456");
        httpRequest.getParamObjectMap().put("type", "0");*/

        //示例-更新 PUT请求数据
        /*httpRequest.setMethod("PUT");
        httpRequest.setURL("http://test.wmfs.com:8008/wm/dir");
        httpRequest.getParamObjectMap().put("id", "16");
        httpRequest.getParamObjectMap().put("pid", "1");
        httpRequest.getParamObjectMap().put("name", "88888");*/

        //示例-查询数据
        httpRequest.setMethod("GET");
        httpRequest.setURL("http://test.wmfs.com:8008/wm/dir");
        httpRequest.getParamMap().put("id", "1");
        httpRequest.getParamMap().put("page", "1");
        httpRequest.getParamMap().put("page_size", "3");

        //示例-表单上传 POST form-data上传图片及参数
        /*httpRequest.setURL("http://test.wmfs.com:8008/wm/file");
        httpRequest.setMethod("POST_FORM");
        httpRequest.getParamObjectMap().put("dirName", "测试目录");
        httpRequest.setFilePath("D:\\test.png");*/

        //示例-删除数据 DELETE请求数据
        /*Integer[] ids = {5};
        httpRequest.setMethod("DELETE_JSON");
        httpRequest.getParamObjectMap().put("ids", ids);*/

        //响应数据
        Response response = null;
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpRequest);
            //json字符串转对象
            String content = httpResponse.getContent();
            Map map = JsonUtil.toMap(content);
            response = JSONObject.parseObject(httpResponse.getContent(), Response.class);
            response.setMessage(String.valueOf(map.get("msg")));
        } catch (Exception exception) {
            //异常处理
            return new Response(ResultCode.FAILURE.getCode(), exception.getMessage());
        }

        return response;
    }

    /**
     * 事件发布 示例
     */
    @GetMapping("publish-event")
    public String publishEvent() {
        applicationEventPublisher.publishEvent(new TestEvent("测试事件"));
        return "测试事件发布成功";
    }

    /**
     * XML解析
     */
    @GetMapping("xml")
    public Response parseXml() {

        List<City> list = null;
        try {
            list = cityService.listCity();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Response<>(200, "请求成功", list);
    }

    /**
     * 对象之间快递转换
     *
     * @return
     */
    @GetMapping("/po-vo")
    public ResDTO changeObject() {

        Balance balance = new Balance(1, 10, 32456);
        //对象之间快递转换
        BalanceVo balanceVo = dozerMapper.map(balance, BalanceVo.class);

        return ResDTO.data(balanceVo);
    }

    /**
     * 统一响应数据封装
     */
    @GetMapping("/show")
    public Response /*JSONObject*/ show(@RequestParam(value = "name", required = true) String name) {
        //统一响应数据封装 方式二
        JSONObject jsonResponse = new JSONObject();
        try {
            // 业务逻辑代码
            jsonResponse.put("code", 0);
            jsonResponse.put("msg", "操作成功！");
            jsonResponse.put("data", "测试数据");
        } catch (Exception e) {
            jsonResponse.put("code", 500);
            jsonResponse.put("msg", "系统异常，请联系管理员！");
        }

        //使用简单的json格式返回
        //return jsonResponse;
        //使用统一的格式返回响应信息
        return new Response(200, "请求成功", jsonResponse);
    }

    /**
     * 获取redis分布式锁 + 属性接口注入
     */
    @GetMapping("set-nx")
    public Response redisSetNx() {

        //jedis 获取分布式锁
        Boolean result = redisUtil.setNx("test:set:nx", "789456");
        //springboot redis 获取分布式锁
        String token = RedisUtil.lock("text:boot", 5000, 5000);

        //方式一 通过value获取配置属性
        log.info("name {}, age {}", name, age);
        //注入属性的几种方式
        log.info("demoOneProperties {}", demoOne);
        log.info("demoTwoProperties {}", demoTwo);
        log.info("demoThreeProperties {}", demoThree);
        log.info("demoFourProperties {}", demoFour);
        //通过环境变量获取
        log.info("demoFiveProperties {}", env.getProperty("demo-env.sex"));

        return new Response<>(200, "请求成功", token);
    }

    /**
     * 参数格式化校验 student内部bean school传递参数例如school.name, school.name
     * groups 使用分组校验参数(IdGroup.class)
     *
     * @param student
     * @return
     * @RequestBody参数json形式接收
     */
    @PostMapping("/validate")
    public Response validate(@Validated/*使用分组校验参数(IdGroup.class)*/ /*@RequestBody参数json形式接收*/ Student student) {

        log.info("学生信息:{}", student);
        return new Response(200, "请求成功", student);
    }
}
