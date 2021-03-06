package com.omerozer.knit.components.graph;

/**
 * Created by omerozer on 2/16/18.
 */

public class UserCounter {

    private int count = 0;

    public void use() {
        count++;
    }

    public void release() {
        count = count > 0 ? count-1 : 0;
    }

    public int getCount(){
        return count;
    }

    public boolean isUsed() {
        return count > 0;
    }

}
