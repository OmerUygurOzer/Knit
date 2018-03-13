package com.omerozer.knit.components.graph;

/**
 * Created by bo_om on 3/12/2018.
 */

public class SingletonUserCounter extends UserCounter {

    private boolean isUsed = false;

    @Override
    public void use() {
        super.use();
        isUsed = true;
    }

    @Override
    public void release() {

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isUsed() {
        return isUsed;
    }
}
