package com.omerozer.knit.schedulers;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by omerozer on 3/13/18.
 */

public class KnitIOReceiverThread {

    private static final String KNIT_LISTENER_THREAD_NAME = "knit_io_receiver";

    private AtomicReference<HandlerThread> receiverThread;
    private volatile boolean isRunning;
    private AtomicReference<Handler> receiverHandler;


    public KnitIOReceiverThread(){
        this.isRunning = false;
        this.receiverThread  = new AtomicReference<>();
        this.receiverHandler = new AtomicReference<>();
    }

    public void start(){
        if(!isRunning){
            this.receiverThread.set(new HandlerThread(KNIT_LISTENER_THREAD_NAME));
            this.receiverThread.get().start();
            this.isRunning = true;
        }
        this.receiverHandler.set(new Handler(receiverThread.get().getLooper()));
    }

    public void shutdown(){
        this.receiverThread.get().quit();
        this.isRunning = false;
    }
    public void post(Runnable runnable){
        this.receiverHandler.get().post(runnable);
    }

}
