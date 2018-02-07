package com.omerozer.knit;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by omerozer on 2/4/18.
 */

public class KnitAsyncTaskHandler {

    private static final int DEFAULT_THREAD_POOL_SIZE = 4;

    ExecutorService threadPool;

    KnitAsyncTaskHandler(){
        this.threadPool = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
    }

    public void submitTask(Runnable runnable){
        this.threadPool.submit(runnable);
    }


}
