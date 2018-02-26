package com.omerozer.knit.schedulers;

import com.omerozer.knit.schedulers.heavy.HeavyTaskScheduler;

/**
 * Created by omerozer on 2/26/18.
 */

public class Schedulers {



    static SchedulerInterface io(){
        return new IOScheduler();
    }

    static SchedulerInterface main(){
        return new AndroidMainThreadScheduler();
    }

    static SchedulerInterface immediate(){
        return new ImmediateScheduler();
    }

    static SchedulerInterface heavy(){
        return new HeavyTaskScheduler();
    }
}
