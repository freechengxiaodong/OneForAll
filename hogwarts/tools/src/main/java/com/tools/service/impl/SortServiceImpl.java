package com.tools.service.impl;

import com.tools.service.SortService;

import java.util.ArrayList;

public class SortServiceImpl implements SortService {

    //二分查找
    public Integer findByBin(int[] list, int left, int right, int source) {
        Integer target = -1;
        if (left > right) {
            return target;
        }

        //第一种
            /*while (left < right) {
                int middle = (left + right) / 2;
                if (source > list[middle]) {
                    left = middle + 1;
                } else if (source < list[middle]) {
                    right = middle - 1;
                } else if (source == list[middle]) {
                    target = middle;
                }
            }*/

        //第二种
        int middle = (left + right) / 2;
        if (source > list[middle]) {
            target = findByBin(list, middle + 1, right, source);
        } else if (source < list[middle]) {
            target = findByBin(list, left, middle - 1, source);
        } else if (source == list[middle]) {
            target = middle;
        }

        return target;
    }

    //冒泡排序
    public Integer[] bubbleSort(Integer[] list) {

        for (int i = 0; i < list.length - 1; i++) {
            for (int j = i; j < (list.length - i - 1); j++) {
                if (list[j + 1] < list[i]) {
                    int tmp;
                    tmp = list[i];
                    list[i] = list[j + 1];
                    list[j + 1] = tmp;
                }
            }
        }

        return list;
    }

    //快速排序
    public ArrayList<Integer> quickSort(ArrayList<Integer> list) {

        if (list.size() <= 1) {
            return list;
        }

        ArrayList left = new ArrayList();
        ArrayList right = new ArrayList();
        ArrayList middle = new ArrayList();
        Integer first = list.get(0);
        middle.add(first);

        for (Integer v : list) {
            if (v.intValue() > first.intValue()) {
                right.add(v);
            } else if (v.intValue() < first.intValue()) {
                left.add(v);
            }
        }

        left = quickSort(left);
        right = quickSort(right);

        left.addAll(middle);
        left.addAll(right);

        return left;
    }

}
