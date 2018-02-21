package com.omerozer.knit;

import android.os.Bundle;

import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;
import com.omerozer.knit.viewevents.handlers.EventHandler;

import java.lang.ref.WeakReference;

/**
 * Created by omerozer on 2/12/18.
 */

public abstract class KnitPresenter<T> implements EventHandler, PresenterInterface {

    private WeakReference<Object> viewObjectRef;

    private Knit knitInstance;

    private InternalModel modelManager;

    private KnitNavigator navigator;

    private Object contract;

    public void setKnit(Knit knit) {
        this.knitInstance = knit;
    }

    @Override
    public void onViewApplied(Object viewObject, Bundle bundle) {
        this.viewObjectRef = new WeakReference<Object>(viewObject);
    }

    private Object getView(){
        return viewObjectRef.get();
    }

    protected void requestData(String data, Object... params) {
        InternalPresenter instance = knitInstance.findPresenterForParent(this);
        getModelManager().request(data, instance, params);
    }

    protected void destroyComponent(){
        knitInstance.destoryComponent(viewObjectRef.get());
    }

    protected void inputData(String data, Object... params) {
        getModelManager().input(data, params);
    }

    protected T getContract() {
        if(contract == null){
            contract = knitInstance.findPresenterForParent(this).getContract();
        }
        return (T) contract;
    }

    protected InternalModel getModelManager() {
        if(modelManager == null){
            modelManager = knitInstance.findPresenterForParent(this).getModelManager();
        }

        return modelManager;
    }

    void setModelManager(InternalModel modelManager) {
        this.modelManager = modelManager;
    }

    void setNavigator(KnitNavigator navigator) {
        this.navigator = navigator;
    }

    void setContract(Object contract) {
        this.contract = contract;
    }

    protected KnitNavigator getNavigator() {
        if(navigator == null){
            navigator =  knitInstance.findPresenterForParent(this).getNavigator();
        }
        return navigator;

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
    public void onCurrentViewReleased() {
        contract = null;
    }

}
