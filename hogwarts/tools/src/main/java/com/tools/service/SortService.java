package com.tools.service;

import java.util.ArrayList;

public interface SortService {

    //二分查找
    public Integer findByBin(int[] list, int left, int right, int source);

    //冒泡排序
    public Integer[] bubbleSort(Integer[] list);

    //快速排序
    public ArrayList<Integer> quickSort(ArrayList<Integer> list);
}