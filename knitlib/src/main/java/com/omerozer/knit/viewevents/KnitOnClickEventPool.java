package com.omerozer.knit.viewevents;

/**
 * Created by omerozer on 2/7/18.
 */

public class KnitOnClickEventPool extends ViewEventPool<KnitOnClickEvent> {

    @Override
    protected KnitOnClickEvent createNewInstance() {
        return new KnitOnClickEvent();
    }
}
