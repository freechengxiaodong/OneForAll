package com.payment.utils;

import com.payment.entity.Balance;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ListMapUtil {
    public static List showArrayList() {

        List list = new ArrayList<>();
        list.add(new String("luojiahui"));
        list.add(new String("luojiafeng"));
        list.add(new Balance(1, 2, 1, "111"));

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
