package com.omerozer.knit.viewevents;

/**
 * Created by omerozer on 2/15/18.
 */

public class KnitOnFocusChangedEventPool extends ViewEventPool<KnitOnFocusChangedEvent> {
    @Override
    protected KnitOnFocusChangedEvent createNewInstance() {
        return new KnitOnFocusChangedEvent();
    }

    @Override
    protected int getMaxPoolSize() {
        return 2;
    }
}
