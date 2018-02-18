package com.omerozer.knit;

import com.omerozer.knit.generators.Callback;

/**
 * Created by omerozer on 2/3/18.
 */

public abstract class InternalModel implements ModelInterface {
    public abstract void request(String data, InternalPresenter presenter, Object... params);

    public abstract void request(String data, Callback callback, Object... params);

    public abstract void input(String data, Object... params);

    public abstract KnitModel getParent();

    public abstract String[] getHandledValues();

    @Override
    public void onCreate() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onMemoryLow() {

    }

    @Override
    public boolean shouldLoad() {
        return false;
    }
}
