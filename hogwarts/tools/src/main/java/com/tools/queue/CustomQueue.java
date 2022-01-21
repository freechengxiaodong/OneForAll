package com.tools.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class CustomQueue {

    private volatile int size = 10;
    private volatile int pushIndex = 0;
    private volatile int popIndex = 0;
    private volatile int currentPosition = 0;
    private Object[] list = new Object[size];
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition full = lock.newCondition();
    private final Condition empty = lock.newCondition();

    public static void main(String[] strings) {

        CustomQueue customQueue = new CustomQueue();
        customQueue.push(1);
        log.info("pop {}", customQueue.pop());
        customQueue.push(2);
        log.info("pop {}", customQueue.pop());
        customQueue.push(3);
        customQueue.push(4);
        log.info("pop {}", customQueue.pop());
        log.info("pop {}", customQueue.pop());
        customQueue.push(5);
        log.info("pop {}", customQueue.pop());

    }

    public Object[] push(Object object) {

        lock.lock();
        try {

            while (currentPosition == size) {
                full.await();
            }

            list[pushIndex] = object;
            if (++pushIndex == size) {
                pushIndex = 0;
            }
            currentPosition++;
            empty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        //log.info("list {}", Arrays.asList(list));
        return list;
    }

    public Object pop() {

        lock.lock();
        Object object = null;
        try {

            while (currentPosition == 0) {
                empty.await();
            }

            object = list[popIndex];
            if (++popIndex == size) {
                popIndex = 0;
            }
            currentPosition--;
            full.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return object;
    }
}
