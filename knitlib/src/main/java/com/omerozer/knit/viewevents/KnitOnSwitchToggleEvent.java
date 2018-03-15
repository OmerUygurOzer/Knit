package com.omerozer.knit.viewevents;

import android.view.View;

/**
 * Created by omerozer on 3/15/18.
 */

public class KnitOnSwitchToggleEvent extends ViewEventEnv {

    private boolean toggle;

    public KnitOnSwitchToggleEvent(String tag, View view) {
        super(tag, view);
    }

    public KnitOnSwitchToggleEvent() {
        super();
    }

    public void setToggle(boolean toggle){
        this.toggle = toggle;
    }

    public boolean getToggle(){
        return toggle;
    }
}
