package com.payment.executor;

import java.util.concurrent.*;

class Executors {
    static ExecutorService newFixedThreadPool(int nThreads) {
        System.out.println("吞吐量优先：线程个数有限，任务个数不限");
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>());
    }

    static ExecutorService newCachedThreadPool() {
        System.out.println("响应时间优先：线程个数不限，任务超时时间1s");
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>());
    }

    static ExecutorService newSingleThreadExecutor() {
        System.out.println("任务执行串行化：FIFO");
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>());
    }
}