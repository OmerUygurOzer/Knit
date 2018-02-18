package com.omerozer.knit;

import android.os.Bundle;

import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;
import com.omerozer.knit.viewevents.handlers.EventHandler;

/**
 * Created by omerozer on 2/12/18.
 */

public abstract class KnitPresenter<T> implements EventHandler, PresenterInterface {

    protected void requestData(String data, Object... params) {
        InternalPresenter instance = Knit.findPresenterForParent(this);
        instance.getModelManager().request(data, instance, params);
    }

    protected void inputData(String data, Object... params) {
        InternalPresenter instance = Knit.findPresenterForParent(this);
        instance.getModelManager().input(data, params);
    }

    protected T getContract() {
        return (T) Knit.findPresenterForParent(this).getContract();
    }

    protected InternalModel getModelManager() {
        return Knit.findPresenterForParent(this).getModelManager();
    }

    protected KnitNavigator getNavigator(){
        return Knit.findPresenterForParent(this).getNavigator();
    }

    @Override
    public void handle(ViewEventPool eventPool, ViewEventEnv eventEnv, InternalModel modelManager) {

    }

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
    public boolean shouldLoad() {
        return false;
    }

    @Override
    public void onMemoryLow() {

    }

    @Override
    public void onViewApplied(Object viewObject, Bundle bundle) {

    }

    @Override
    public void onCurrentViewReleased() {

    }
}
