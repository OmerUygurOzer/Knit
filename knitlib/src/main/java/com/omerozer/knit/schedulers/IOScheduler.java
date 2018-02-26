package com.omerozer.knit.schedulers;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by omerozer on 2/23/18.
 */

public class IOScheduler implements SchedulerInterface {

    private static final int DEFAULT_THREAD_POOL_SIZE = 4;
    private static final String KNIT_LISTENER_THREAD_NAME = "knit_io_receiver";
    private static  AtomicReference<ExecutorService> THREAD_POOL;
    private static final long TIME_OUT = 20L;

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

    class ListenerThread extends Thread implements Runnable{

        volatile boolean running;
        LinkedBlockingQueue<Future<?>> futureRegistry;
        AtomicReference<Handler> receiverHandler;

        private ListenerThread() {
            super();
            this.futureRegistry = new LinkedBlockingQueue<>();
        }

        void registerFuture(Future<?> future){
            try {
                futureRegistry.put(future);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        void setReceiverHandler(AtomicReference<Handler> handler){
            this.receiverHandler = handler;
        }

        @Override
        public void run() {
            while (running){
                Future<?> future = null;
                try {
                    future = futureRegistry.take();
                    while (!future.isDone()){
                        sleep(1);
                    }
                    final Object data = future.get(TIME_OUT, TimeUnit.SECONDS);
                    if(target.get()!=null){
                        target.get().start();
                        target.get().submit(new Runnable() {
                                                @Override
                                                public void run() {
                                                     resultConsumer.get().consumer(data);
                                                }
                                            }

                        );
                        isDone.set(true);
                    }

                } catch (InterruptedException e) {
                    errorOut(e);
                } catch (ExecutionException e) {
                    errorOut(e);
                    future.cancel(true);
                } catch (TimeoutException e) {
                    errorOut(e);
                    future.cancel(true);
                }

            }
        }

        private void errorOut(final Throwable throwable){
            if(target.get()!=null){
                target.get().start();
                target.get().submit(new Runnable() {
                    @Override
                    public void run() {
                        resultConsumer.get().error(throwable);
                    }
                });
            }
        }

        private void shutdown(){
            running = false;
        }

    }


    private AtomicReference<HandlerThread> receiverThread;
    private AtomicReference<Handler> receiverHandler;
    private AtomicReference<ListenerThread> listenerThread;
    private AtomicReference<SchedulerInterface> target;
    private AtomicReference<Consumer> resultConsumer;
    private AtomicBoolean isDone;

    IOScheduler(){
        init();
        this.receiverThread  = new AtomicReference<>(new HandlerThread(KNIT_LISTENER_THREAD_NAME));
        this.receiverHandler = new AtomicReference<>();
        this.listenerThread = new AtomicReference<>();
        this.target = new AtomicReference<>();
        this.resultConsumer = new AtomicReference<>();
        this.isDone = new AtomicBoolean(false);
    }



    @Override
    public<T> void submit(Callable<T> callable) {
        listenerThread.get().registerFuture(THREAD_POOL.get().submit(callable));
    }

    @Override
    public void submit(Runnable runnable) {
        listenerThread.get().registerFuture(THREAD_POOL.get().submit(runnable));
    }


    @Override
    public void start() {
        this.listenerThread.set(new ListenerThread());
        this.receiverThread.get().start();
        this.receiverHandler.set(new Handler(receiverThread.get().getLooper()));
        this.listenerThread.get().setReceiverHandler(receiverHandler);
        this.listenerThread.get().start();
    }



    @Override
    public void shutDown() {
        this.listenerThread.get().shutdown();
        this.receiverThread.get().quit();
    }

    @Override
    public boolean isDone() {
        return isDone.get();
    }

    @Override
    public void setTargetAndConsumer(SchedulerInterface schedulerInterface,
            Consumer consumer) {
        this.target.set(schedulerInterface);
        this.resultConsumer.set(consumer);
    }
}
