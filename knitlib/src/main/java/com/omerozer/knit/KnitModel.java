package com.omerozer.knit;

/**
 * Created by omerozer on 2/3/18.
 */

public interface KnitModel {
    void request(String[] params,KnitPresenter presenter);
    String[] getHandledValues();
}
