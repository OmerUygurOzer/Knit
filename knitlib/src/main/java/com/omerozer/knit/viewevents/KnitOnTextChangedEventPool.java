package com.omerozer.knit.viewevents;

/**
 * Created by omerozer on 2/7/18.
 */

public class KnitOnTextChangedEventPool extends ViewEventPool<KnitTextChangedEvent> {
    @Override
    protected KnitTextChangedEvent createNewInstance() {
        return new KnitTextChangedEvent();
    }
}
