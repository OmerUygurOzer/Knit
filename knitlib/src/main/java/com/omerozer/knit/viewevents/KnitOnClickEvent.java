package com.omerozer.knit.viewevents;

import android.view.View;

/**
 * Created by omerozer on 2/7/18.
 */

public class KnitOnClickEvent extends ViewEventEnv {

    public KnitOnClickEvent(String tag, View view) {
        super(tag, view);
    }

    public KnitOnClickEvent() {
        super();
    }

}
