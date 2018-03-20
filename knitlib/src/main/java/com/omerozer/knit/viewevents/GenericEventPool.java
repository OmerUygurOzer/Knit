package com.omerozer.knit.viewevents;

/**
 * Created by Omer Ozer on 3/13/2018.
 */

public class GenericEventPool extends ViewEventPool<GenericEvent> {
    @Override
    protected GenericEvent createNewInstance() {
        return new GenericEvent();
    }

    @Override
    protected int getMaxPoolSize() {
        return 4;
    }
}
