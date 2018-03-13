package com.omerozer.knit;

import com.omerozer.knit.schedulers.KnitIOReceiverThread;

/**
 * Created by omerozer on 3/13/18.
 */

public class TestKnitIOReceiverThread extends KnitIOReceiverThread {

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void post(Runnable runnable) {
        runnable.run();
    }
}
