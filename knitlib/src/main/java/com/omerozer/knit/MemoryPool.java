package com.omerozer.knit;

import java.util.Stack;

/**
 * Created by omerozer on 3/16/18.
 */

public abstract class MemoryPool<T extends Poolable> {
    private static final int MAX = 5;

    Stack<T> events;

    public MemoryPool(){
        events = new Stack<>();
    }

    public T getObject(){
        if(events.isEmpty()){
            return createNewInstance();
        }
        return events.pop();
    }

    public void pool(T event){
        event.recycle();
        if(events.size()< getPoolSize()){
            events.push(event);
        }
    }

    protected abstract T createNewInstance();

    protected int getPoolSize(){
        return MAX;
    }
}
