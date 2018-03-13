package com.omerozer.knit.viewevents;

import android.view.View;

/**
 * Created by omerozer on 3/13/18.
 */

public class KnitOnRefreshEvent extends ViewEventEnv {

    public KnitOnRefreshEvent(String tag, View view) {
        super(tag, view);
    }

    public KnitOnRefreshEvent() {
        super();
    }

}
