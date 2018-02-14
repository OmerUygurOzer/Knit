package com.omerozer.sample.presenters;

import android.os.Bundle;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.KnitNavigator;
import com.omerozer.knit.KnitPresenter;
import com.omerozer.knit.Presenter;
import com.omerozer.knit.Updating;
import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;
import com.omerozer.sample.views.MainActivity;
import com.omerozer.sample.views.SecondActivity;


/**
 * Created by omerozer on 2/2/18.
 */

@Presenter(MainActivity.class)
public class MainPresenter extends KnitPresenter<MainActivity> {


    @Override
    public void onCreate() {

    }

    @Override
    public void onViewApplied(Object viewObject,Bundle data){
        requestData("testN","BOO ");
    }

    @Override
    public void onCurrentViewReleased() {

    }

    @Override
    public void handle(ViewEventPool eventPool, ViewEventEnv eventEnv, InternalModel modelManager) {
        if(eventEnv.getTag().equals("button")){
            getView().recMes("BUTTON PRESSED");
            requestData("test");
        }

        KnitNavigator
                .toActivity()
                .setContext(getView())
                .toActivity(SecondActivity.class)
                .go();

        eventPool.pool(eventEnv);
    }

    @Updating("test")
    void updateData1(String data){
        getView().recMes(data);
    }

    @Updating("testN")
    void updateData2(String data){
        getView().recMes(data);
    }

}
