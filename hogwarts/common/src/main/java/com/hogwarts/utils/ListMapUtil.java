package com.hogwarts.utils;

import lombok.extern.slf4j.Slf4j;
import java.util.*;

@Slf4j
public class ListMapUtil {

    public static void main(String[] args) {

        //遍历HashMap测试
        showHashMap();
    }

    public static Map showHashMap() {
        //定义HashMap
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(1, 123456);
        map.put(2, 789456);
        map.put(3, 100000);

        /****************方式一  使用For-Each迭代entries*******************/
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            log.info("方式一 key {}, value {}", entry.getKey(), entry.getValue());
        }

        /****************方式二  使用For-Each迭代keys和values*******************/
        //iterating over keys only
        for (Integer key : map.keySet()) {
            log.info("方式二 key {}", key);
        }
        //iterating over values only
        for (Integer value : map.values()) {
            log.info("方式二 Value {}", value);
        }

        /****************方式三 使用Iterator迭代*******************/
        //使用泛型
        Iterator<Map.Entry<Integer, Integer>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Integer> entry = entries.next();
            log.info("方式三 使用泛型 key {}, value {}", entry.getKey(), entry.getValue());
        }
        //不使用泛型
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Integer key = (Integer) entry.getKey();
            Integer value = (Integer) entry.getValue();
            log.info("方式三 不使用泛型 key {}, value {}", key, value);
        }

        /****************方式四 迭代keys并搜索values（低效的）*******************/
        for (Integer key : map.keySet()) {
            Integer value = map.get(key);
            log.info("方式四 key {}, value {}", key, value);
        }

        return map;
    }

    public static List showArrayList() {

        List list = new ArrayList<>();
        list.add(new String("luojiahui"));
        list.add(new String("luojiafeng"));
        //list.add(new Balance(1, 2, 1, "111"));

        //方法1
        Iterator it1 = list.iterator();
        while (it1.hasNext()) {
            System.out.println(it1.next());
        }

        //方法2
        for (Iterator it2 = list.iterator(); it2.hasNext(); ) {
            System.out.println(it2.next());
        }

        //方法3
        /*for(String tmp:list){
            System.out.println(tmp);
        }*/

        //方法4
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        return list;
    }

    public static LinkedList<String> showLinkList() {
        // 实例化
        LinkedList<String> linkedList = new LinkedList<>();
        // 添加元素
        linkedList.add("1");
        linkedList.add("2");
        linkedList.add("3");

        // 用addFirst方法向链表集合开头添加一个元素
        linkedList.addFirst("开头");   //push()方法和addFirst()方法功能一样
        // 用addLast方法向链表集合开头添加一个元素
        linkedList.addLast("结尾");

        // 移除并返回此列表的第一个元素
        String first = linkedList.removeFirst(); // pop()方法和removeFirst()方法功能一样
        // 移除并返回此列表的最后一个元素
        String last = linkedList.removeLast();

        // 获取第一个元素
        String start = linkedList.getFirst();
        // 获取最后一个元素
        String end = linkedList.getLast();

        System.out.println(first);
        System.out.println(last);
        System.out.println(start);
        System.out.println(end);

        //测试
        Map map = new HashMap();
        map.put("ddd", 1);

        HashSet set = new HashSet();
        set.add(1);

        return linkedList;
    }
}
