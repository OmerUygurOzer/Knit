package com.omerozer.knit.schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by omerozer on 2/26/18.
 */

public class ImmediateScheduler implements SchedulerInterface {

    private AtomicReference<SchedulerInterface> target;
    private AtomicReference<Consumer> resultConsumer;
    private AtomicBoolean isDone;


    public ImmediateScheduler(){
        this.target = new AtomicReference<>();
        this.resultConsumer = new AtomicReference<>();
        this.isDone = new AtomicBoolean(false);
    }

    @Override
    public <T> void submit(Callable<T> callable) {
        try {
            final T data = callable.call();
            if(target.get()!=null){
                target.get().start();
                target.get().submit(new Runnable() {
                    @Override
                    public void run() {
                        resultConsumer.get().consume(data);
                    }
                });

            }
            isDone.set(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void submit(Runnable runnable) {
        runnable.run();
        this.isDone.set(true);
    }

    @Override
    public void start() {
        EVICTOR_THREAD.registerScheduler(this);
    }

    @Override
    public void shutDown() {

    }

    @Override
    public <T> void setTargetAndConsumer(SchedulerInterface schedulerInterface, Consumer consumer) {
        this.target.set(schedulerInterface);
        this.resultConsumer.set(consumer);
    }

    @Override
    public boolean isDone() {
        return isDone.get();
    }
}
