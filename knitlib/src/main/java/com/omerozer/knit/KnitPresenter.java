package com.omerozer.knit;

import com.omerozer.knit.viewevents.handlers.EventHandler;

/**
 * Created by omerozer on 2/12/18.
 */

public abstract class KnitPresenter<T> implements EventHandler, PresenterInterface {

    protected void requestData(String data, Object... params) {
        InternalPresenter instance = Knit.findPresenterForParent(this);
        instance.getModelManager().request(data, instance, params);
    }

    protected T getContract() {
        return (T) Knit.findPresenterForParent(this).getContract();
    }

    protected InternalModel getModelManager() {
        return Knit.findPresenterForParent(this).getModelManager();
    }


}
