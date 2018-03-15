package com.omerozer.knit.viewevents;

/**
 * Created by omerozer on 3/15/18.
 */

public class KnitOnSwitchToggleEventPool extends ViewEventPool<KnitOnSwitchToggleEvent> {
    @Override
    protected KnitOnSwitchToggleEvent createNewInstance() {
        return new KnitOnSwitchToggleEvent();
    }
}
