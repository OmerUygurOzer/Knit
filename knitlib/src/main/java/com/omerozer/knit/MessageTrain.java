package com.omerozer.knit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omerozer on 3/16/18.
 */

public class MessageTrain {

    private Map<Class<?>,KnitMessage> messageMap;

    MessageTrain(){
        this.messageMap = new HashMap<>();
    }

    public void putMessageForView(Class<?> view,KnitMessage message){
        this.messageMap.put(view,message);
    }

    public boolean hasMessage(Class<?> view){
        return this.messageMap.containsKey(view);
    }

    public KnitMessage getMessageForView(Class<?> view){
        KnitMessage message = this.messageMap.get(view);
        this.messageMap.remove(view);
        return message;
    }
}
