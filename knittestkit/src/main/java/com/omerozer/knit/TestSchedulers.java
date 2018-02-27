package com.omerozer.knit;

import com.omerozer.knit.schedulers.ImmediateScheduler;
import com.omerozer.knit.schedulers.SchedulerInterface;
import com.omerozer.knit.schedulers.SchedulerProvider;

/**
 * Created by omerozer on 2/26/18.
 */

public class TestSchedulers implements SchedulerProvider {
    @Override
    public SchedulerInterface io() {
        return new ImmediateScheduler();
    }

    @Override
    public SchedulerInterface main() {
        return new ImmediateScheduler();
    }

    @Override
    public SchedulerInterface immediate() {
        return new ImmediateScheduler();
    }

    @Override
    public SchedulerInterface heavy() {
        return new ImmediateScheduler();
    }
}
