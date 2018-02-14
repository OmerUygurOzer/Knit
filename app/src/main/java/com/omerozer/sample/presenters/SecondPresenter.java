package com.omerozer.sample.presenters;

import android.os.Bundle;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.KnitPresenter;
import com.omerozer.knit.Presenter;
import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;
import com.omerozer.sample.views.SecondActivity;

/**
 * Created by omerozer on 2/6/18.
 */

@Presenter(SecondActivity.class)
public class SecondPresenter extends KnitPresenter<SecondActivity> {


    @Override
    public void onCreate() {

    }

    @Override
    public void onViewApplied(Object viewObject,Bundle bundle){

    }

    @Override
    public void onCurrentViewReleased() {

    }

    @Override
    public void handle(ViewEventPool eventPool, ViewEventEnv eventEnv, InternalModel modelManager) {

    }
}
