package com.omerozer.knit.schedulers.heavy;

import static com.omerozer.knit.schedulers.heavy.HeavyThread.getPriority;
import static com.omerozer.knit.schedulers.heavy.HeavyThread.handleTask;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.omerozer.knit.Knit;
import com.omerozer.knit.schedulers.Consumer;
import com.omerozer.knit.schedulers.SchedulerInterface;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by omerozer on 2/26/18.
 */

public class HeavyTaskScheduler implements SchedulerInterface {

    static final String HEAVY_THREAD_NAME1 = "knit_heavy_thread1";
    static final String HEAVY_THREAD_NAME2 = "knit_heavy_thread2";
    static final String HEAVY_THREAD_NAME3 = "knit_heavy_thread3";
    static final String HEAVY_THREAD_NAME4 = "knit_heavy_thread4";

    private static final Object queueLock;
    private static final PriorityQueue<AvailableThread> avaiableThreads;

    static {
        queueLock = new Object();
        avaiableThreads = new PriorityQueue<>(4,getThreadPriorityComparator());
        avaiableThreads.offer(new AvailableThread(HThread1.class, HEAVY_THREAD_NAME1));
        avaiableThreads.offer(new AvailableThread(HThread2.class, HEAVY_THREAD_NAME2));
        avaiableThreads.offer(new AvailableThread(HThread3.class, HEAVY_THREAD_NAME3));
        avaiableThreads.offer(new AvailableThread(HThread4.class, HEAVY_THREAD_NAME4));
    }

    private static class AvailableThread{
        public Class<? extends HeavyThread> clazz;
        public String threadId;

        AvailableThread(Class<? extends HeavyThread> clazz, String threadId) {
            this.clazz = clazz;
            this.threadId = threadId;
        }

    }

    private static Comparator<AvailableThread> getThreadPriorityComparator(){
        return new Comparator<AvailableThread>() {
            @Override
            public int compare(AvailableThread o1,
                    AvailableThread o2) {
                return (getPriority(o1.threadId) < getPriority(o2.threadId)) ? -1 : ((getPriority(o1.threadId)
                        == getPriority(o2.threadId)) ? 0 : 1);
            }
        };
    }


    private SchedulerInterface target;
    private Consumer consumer;
    private Context context;

    public HeavyTaskScheduler() {
        this.context = Knit.getInstance().getApp();
    }

    @Override
    public <T> void submit(Callable<T> callable) {
        TaskPackage taskPackage = new TaskPackage();
        taskPackage.setCallable(callable);
        taskPackage.setTarget(target);
        taskPackage.setConsumer(consumer);
        AvailableThread availableThread = getLeastBusyThread();
        handleTask(availableThread.threadId, taskPackage, context, availableThread.clazz);
    }

    @Override
    public void submit(Runnable runnable) {
        TaskPackage taskPackage = new TaskPackage();
        taskPackage.setRunnable(runnable);
        taskPackage.setTarget(target);
        taskPackage.setConsumer(consumer);
        AvailableThread availableThread =  getLeastBusyThread();
        handleTask(availableThread.threadId, taskPackage, context, availableThread.clazz);
    }

    private AvailableThread getLeastBusyThread(){
        synchronized (queueLock) {
            AvailableThread availableThread = avaiableThreads.poll();
            avaiableThreads.offer(availableThread);
            return availableThread;
        }
    }

    @Override
    public void start() {
        EVICTOR_THREAD.registerScheduler(this);
    }

    @Override
    public void shutDown() {
        context.stopService(new Intent(context, HThread1.class));
        context.stopService(new Intent(context, HThread2.class));
        context.stopService(new Intent(context, HThread3.class));
        context.stopService(new Intent(context, HThread4.class));

    }

    @Override
    public <T> void setTargetAndConsumer(SchedulerInterface schedulerInterface, Consumer consumer) {
        this.target = schedulerInterface;
        this.consumer = consumer;
    }

    @Override
    public boolean isDone() {
        return  getPriority(HEAVY_THREAD_NAME1) == 0 &&
                getPriority(HEAVY_THREAD_NAME2) == 0 &&
                getPriority(HEAVY_THREAD_NAME3) == 0 &&
                getPriority(HEAVY_THREAD_NAME4) == 0;
    }
}
