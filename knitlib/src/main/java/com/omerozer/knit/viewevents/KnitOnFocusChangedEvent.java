package com.omerozer.knit.viewevents;

import android.view.View;

import java.util.HashMap;

/**
 * Created by omerozer on 2/15/18.
 */

public class KnitOnFocusChangedEvent extends ViewEventEnv {

    private static final String HAS_FOCUS = "b";

    public KnitOnFocusChangedEvent(String tag, View view) {
        super(tag, view);
    }

    public KnitOnFocusChangedEvent() {
        super();
    }

    public boolean hasFocus(){
        return getDataBundle().getBoolean(HAS_FOCUS);
    }

    public void setFocus(boolean b){
        getDataBundle().putBoolean(HAS_FOCUS,b);
    }


}
