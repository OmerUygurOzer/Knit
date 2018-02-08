package com.omerozer.knit.viewevents;

import java.util.Stack;

/**
 * Created by omerozer on 2/7/18.
 */

public abstract class ViewEventPool<T extends ViewEventEnv> {

    private static final int MAX = 5;

    Stack<T> events;

    public ViewEventPool(){
        events = new Stack<>();
    }

    public T getEvent(){
        if(events.isEmpty()){
            return createNewInstance();
        }
        return events.pop();
    }

    public void pool(T event){
        if(events.size()< MAX){
            events.clear();
            events.push(event);
        }
    }

    protected abstract T createNewInstance();



}
