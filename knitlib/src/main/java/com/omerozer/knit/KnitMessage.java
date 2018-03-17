package com.omerozer.knit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omerozer on 3/16/18.
 */

public class KnitMessage implements Poolable {

    private Map<String,Object> data;

    KnitMessage(){
        this.data = new HashMap<>();
    }

    public KnitMessage putData(String key, Object value){
        this.data.put(key,value);
        return this;
    }

    public<T> T getData(String key){
        if(!data.containsKey(key)){
            throw new RuntimeException("Knit: Data('" + key + "' ) could not be found in the message");
        }
        return (T)data.get(key);
    }

    private void clear(){
        this.data.clear();
    }


    @Override
    public void recycle() {
        clear();
    }
}
