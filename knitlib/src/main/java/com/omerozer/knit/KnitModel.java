package com.omerozer.knit;

/**
 * Created by omerozer on 2/12/18.
 */

public abstract class KnitModel implements ModelInterface  {

    InternalModel modelManager;

    public void setModelManager(InternalModel internalModel){
        this.modelManager = internalModel;
    }

    protected <T>KnitResponse<T> requestImmediately(String data,Object... params){
        return (KnitResponse<T>) modelManager.requestImmediately(data,params);
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
    public void onMemoryLow() {

    }

    @Override
    public boolean shouldLoad() {
        return false;
    }
}
