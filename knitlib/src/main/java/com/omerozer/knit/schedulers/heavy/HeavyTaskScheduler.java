package com.omerozer.knit.schedulers.heavy;

import com.omerozer.knit.schedulers.Consumer;
import com.omerozer.knit.schedulers.SchedulerInterface;

import java.util.concurrent.Callable;

/**
 * Created by omerozer on 2/26/18.
 */

public class HeavyTaskScheduler implements SchedulerInterface {

    private static final String HEAVY_THREAD_NAME = "knit_heavy_thread";

    @Override
    public <T> void submit(Callable<T> callable) {

    }

    @Override
    public void submit(Runnable runnable) {

    }

    @Override
    public void start() {

    }

    @Override
    public void shutDown() {

    }

    @Override
    public <T> void setTargetAndConsumer(SchedulerInterface schedulerInterface, Consumer consumer) {

    }

    @Override
    public boolean isDone() {
        return false;
    }
}
