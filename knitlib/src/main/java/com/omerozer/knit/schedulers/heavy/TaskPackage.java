package com.omerozer.knit.schedulers.heavy;

import com.omerozer.knit.schedulers.Consumer;
import com.omerozer.knit.schedulers.SchedulerInterface;

import java.util.concurrent.Callable;

/**
 * Created by omerozer on 2/28/18.
 */

public class TaskPackage {

    public static final TaskPackage EMPTY = new TaskPackage();

    private SchedulerInterface target;
    private Consumer consumer;
    private Runnable runnable;
    private Callable callable;

    public SchedulerInterface getTarget() {
        return target;
    }

    public void setTarget(SchedulerInterface target) {
        this.target = target;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public Callable getCallable() {
        return callable;
    }

    public void setCallable(Callable callable) {
        this.callable = callable;
    }
}
