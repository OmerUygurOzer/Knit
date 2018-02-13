package com.omerozer.knit;

/**
 * Created by omerozer on 2/13/18.
 */

public interface PresenterInterface {
    void onCreate();
    void onViewApplied(Object viewObject);
    void onCurrentViewReleased();
}
