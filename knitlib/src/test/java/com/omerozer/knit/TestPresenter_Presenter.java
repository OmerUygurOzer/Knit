package com.omerozer.knit;

import android.content.Intent;
import android.os.Bundle;

import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;

/**
 * Created by omerozer on 3/8/18.
 */

public class TestPresenter_Presenter extends InternalPresenter {

    private Knit knit;

    private KnitNavigator knitNavigator;

    private InternalModel modelManager;

    public TestPresenter_Presenter(){

    }

    public TestPresenter_Presenter(Knit knitInstance, KnitNavigator navigator,
            InternalModel modelManager) {
        this.knit = knitInstance;
        this.knitNavigator = navigator;
        this.modelManager = modelManager;
    }


    public Knit getKnit() {
        return knit;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onMemoryLow() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean shouldLoad() {
        return false;
    }

    @Override
    public void onViewApplied(Object viewObject, Bundle bundle) {

    }

    @Override
    public void onCurrentViewReleased() {

    }

    @Override
    public InternalModel getModelManager() {
        return modelManager;
    }

    @Override
    public KnitNavigator getNavigator() {
        return knitNavigator;
    }

    @Override
    public Object getContract() {
        return null;
    }

    @Override
    public String[] getUpdatableFields() {
        return new String[0];
    }

    @Override
    public KnitPresenter getParent() {
        return null;
    }

    @Override
    public void handle(ViewEventPool eventPool, ViewEventEnv eventEnv, InternalModel modelManager) {

    }

    @Override
    public void onViewStart() {

    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewPause() {

    }

    @Override
    public void onViewStop() {

    }

    @Override
    public void onViewResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onReturnToView() {

    }
}
