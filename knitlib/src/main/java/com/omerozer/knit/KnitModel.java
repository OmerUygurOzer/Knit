package com.omerozer.knit;

import com.omerozer.knit.generators.Callback;

/**
 * Created by omerozer on 2/12/18.
 */

public abstract class KnitModel implements ModelInterface  {

    InternalModel modelManager;

    void setModelManager(InternalModel internalModel){
        this.modelManager = internalModel;
    }

    protected void request(String data,Callback callback,Object... params){
        modelManager.request(data,callback,params);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void load() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean shouldLoad() {
        return false;
    }
}
