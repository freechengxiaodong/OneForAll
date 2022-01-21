package com.payment.contoller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSONObject;
import com.payment.entity.Balance;
import com.payment.entity.BalanceVo;
import com.payment.entity.City;
import com.payment.service.CityService;
import com.payment.utils.ListMapUtil;
import com.payment.utils.ResponseData;
import com.payment.utils.ResponseInfo;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

@RestController
@RefreshScope
public class PaymentController {

    @Value("${sleep:0}")
    private int sleep;

    @Autowired
    CityService cityService;
    /*po dto vo之间快速转换*/
    @Autowired
    Mapper dozerMapper;

    final static Map<Integer, Balance> balanceMap = new HashMap() {
        {
            put(1, new Balance(1, 10, 1000));
            put(2, new Balance(2, 0, 10000));
            put(3, new Balance(3, 100, 0));
        }
    };

    /**
     * 对象之间快递转换
     *
     * @return
     */
    @GetMapping("/pay/po2vo")
    public ResponseData changeObject() {

        Balance balance = new Balance(1, 10, 32456);
        //对象之间快递转换
        BalanceVo balanceVo = dozerMapper.map(balance, BalanceVo.class);

        return ResponseData.data(balanceVo);
    }

    @GetMapping("/pay/balance")
    @SentinelResource(value = "protected-resource", blockHandler = "handleBlock")
    public ResponseData getBalance(Integer id) {
        System.out.println("request: /pay/balance?id=" + id + ", sleep: " + sleep);
        if (sleep > 0) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (id != null && balanceMap.containsKey(id)) {
            return ResponseData.data(balanceMap.get(id));
        }

        //统一响应数据封装 方式一
        return ResponseData.data(new Balance(0, 0, 0));

        //return new Balance(0, 0, 0);
    }

    /*限流函数*/
    public Balance handleBlock(Integer id, BlockException e) {
        return new Balance(0, 0, 0, "限流666");
    }

    /**
     * 统一响应数据封装
     */
    @GetMapping("/pay/show")
    public ResponseInfo /*JSONObject*/ show(@RequestParam(value = "name", required = true) String name) {
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
        return new ResponseInfo(200, "请求成功", jsonResponse);
    }

    /**
     * xml文件解析成对象
     */
    @GetMapping("/pay/xml")
    public ResponseInfo xml() {

        List<City> list = null;
        try {
            list = cityService.listCity();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseInfo(200, "请求成功", list);
    }

    /**
     * list  map测试
     */
    @GetMapping("/pay/list")
    public ResponseInfo show() {

        List list = ListMapUtil.showArrayList();
        LinkedList linkedList = ListMapUtil.showLinkList();

        //
        //1.创建DocumentBuilderFactory对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //2.创建DocumentBuilder对象
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("payment/src/main/resources/cityList.xml"));
            NodeList sList = document.getElementsByTagName("Loggers");
            //element(sList);
            node(sList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseInfo(200, "请求成功", list);
    }

    //读取xml 用Node方式
    public static void node(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            NodeList childNodes = node.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    System.out.print(childNodes.item(j).getNodeName() + ":");
                    System.out.println(childNodes.item(j).getFirstChild().getNodeValue());
                }
            }
        }
    }

    //读取xml 用Element方式
    public static void element(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            NodeList childNodes = element.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    //获取节点
                    System.out.print(childNodes.item(j).getNodeName() + ":");
                    //获取节点值
                    System.out.println(childNodes.item(j).getFirstChild().getNodeValue());
                }
            }
        }
    }

}