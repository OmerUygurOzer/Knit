package com.omerozer.knit.schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by omerozer on 3/13/18.
 */

public class KnitIOThreadPool {

    private static final int DEFAULT_THREAD_POOL_SIZE = 4;
    private static AtomicReference<ExecutorService> THREAD_POOL;

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

    public<T> void submit(Callable<T> callable){
        THREAD_POOL.get().submit(callable);
    }

    public void submit(Runnable runnable){
        THREAD_POOL.get().submit(runnable);
    }

}
