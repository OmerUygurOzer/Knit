package com.omerozer.knit.schedulers;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by omerozer on 2/23/18.
 */

public class IOScheduler implements SchedulerInterface {

    private static final int DEFAULT_THREAD_POOL_SIZE = 4;
    private static final String KNIT_LISTENER_THREAD_NAME = "knit_io_receiver";
    private static  AtomicReference<ExecutorService> THREAD_POOL;

    static {
        THREAD_POOL = new AtomicReference<>();
        init();
    }

    static void init(){
        if(THREAD_POOL.get()!=null && THREAD_POOL.get().isShutdown()){
            THREAD_POOL.set(Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE));
        }else if (THREAD_POOL.get()==null){
            THREAD_POOL.set(Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE));
        }
    }


    private AtomicReference<HandlerThread> receiverThread;
    private AtomicReference<Handler> receiverHandler;
    private AtomicReference<SchedulerInterface> target;
    private AtomicReference<Consumer> resultConsumer;
    private AtomicBoolean isDone;

    public IOScheduler(){
        init();
        this.receiverThread  = new AtomicReference<>(new HandlerThread(KNIT_LISTENER_THREAD_NAME));
        this.receiverHandler = new AtomicReference<>();
        this.target = new AtomicReference<>();
        this.resultConsumer = new AtomicReference<>();
        this.isDone = new AtomicBoolean(false);
    }



    @Override
    public<T> void submit(Callable<T> callable) {
        THREAD_POOL.get().submit(createTask(callable));
    }

    @Override
    public void submit(Runnable runnable) {
        THREAD_POOL.get().submit(runnable);
    }


    @Override
    public void start() {
        EVICTOR_THREAD.registerScheduler(this);
        this.receiverThread.get().start();
        this.receiverHandler.set(new Handler(receiverThread.get().getLooper()));
    }



    @Override
    public void shutDown() {
        this.receiverThread.get().quit();
    }

    @Override
    public boolean isDone() {
        return isDone.get();
    }

    private<T> Runnable createTask(final Callable<T> callable){
        return new Runnable() {
            @Override
            public void run() {
                try {
                    final Object data = callable.call();
                    receiverHandler.get().post(new Runnable() {
                        @Override
                        public void run() {
                            target.get().start();
                            target.get().submit(new Runnable() {
                                @Override
                                public void run() {
                                    resultConsumer.get().consume(data);
                                    isDone.set(true);
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    resultConsumer.get().error(e);

                }
            }
        };
    }

    @Override
    public void setTargetAndConsumer(SchedulerInterface schedulerInterface,
            Consumer consumer) {
        this.target.set(schedulerInterface);
        this.resultConsumer.set(consumer);
    }
}
