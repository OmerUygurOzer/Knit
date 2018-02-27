package com.omerozer.knit.schedulers;

import com.omerozer.knit.schedulers.heavy.HeavyTaskScheduler;

/**
 * Created by omerozer on 2/26/18.
 */

public interface SchedulerProvider {
    SchedulerInterface io();

    SchedulerInterface main();

    SchedulerInterface immediate();

    SchedulerInterface heavy();
}
