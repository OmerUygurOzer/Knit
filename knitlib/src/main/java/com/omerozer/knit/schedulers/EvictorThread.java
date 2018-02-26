package com.omerozer.knit.schedulers;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by omerozer on 2/26/18.
 */

public class EvictorThread implements Runnable {

    private static final long SLEEP_DELAY = 100L;
    private static final long STAY_ALIVE = 20L * 1000L;

    private volatile boolean isRunning;
    private AtomicLong evictBase;
    private Thread thread;
    private ReadWriteLock entryLock;
    private Set<SchedulerInterface> schedulers;

    EvictorThread(){
        this.isRunning = true;
        this.entryLock = new ReentrantReadWriteLock();
        this.schedulers = new LinkedHashSet<>();
    }

    void start(){
        this.thread = new Thread(this);
        this.evictBase = new AtomicLong(System.currentTimeMillis());
        this.thread.start();
    }

    void stop(){
        this.isRunning = false;
    }


    void registerScheduler(SchedulerInterface scheduler){
        this.evictBase.set(System.currentTimeMillis());
        this.entryLock.writeLock().lock();
        this.schedulers.add(scheduler);
        this.entryLock.writeLock().unlock();
    }

    @Override
    public void run() {
        while (isRunning){
            entryLock.readLock().lock();
            Iterator<SchedulerInterface> iterator = schedulers.iterator();
            SchedulerInterface scheduler;
            while (iterator.hasNext()){
                scheduler = iterator.next();
                if(scheduler.isDone()){
                    scheduler.shutDown();
                }
                iterator.remove();
            }
            entryLock.readLock().unlock();

            if(System.currentTimeMillis() - evictBase.get() >= STAY_ALIVE){
                stop();
                this.isRunning = true;
            }

            sleep();
        }
    }

    private void sleep(){
        try {
            Thread.sleep(SLEEP_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
