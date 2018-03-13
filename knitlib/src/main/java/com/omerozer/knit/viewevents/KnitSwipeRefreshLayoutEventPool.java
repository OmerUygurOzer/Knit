package com.omerozer.knit.viewevents;

/**
 * Created by omerozer on 3/13/18.
 */

public class KnitSwipeRefreshLayoutEventPool extends ViewEventPool<KnitOnRefreshEvent> {

    @Override
    protected KnitOnRefreshEvent createNewInstance() {
        return new KnitOnRefreshEvent();
    }

    @Override
    protected int getPoolSize() {
        return 2;
    }
}
