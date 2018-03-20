package com.omerozer.knit.viewevents;

import com.omerozer.knit.MemoryPool;

/**
 * Created by omerozer on 2/7/18.
 */

public abstract class ViewEventPool<T extends ViewEventEnv> extends MemoryPool<T> {

    private static final int MAX = 5;

    protected int getMaxPoolSize(){
        return MAX;
    }

}
