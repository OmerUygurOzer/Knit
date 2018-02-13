package com.omerozer.sample.presenters;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.KnitPresenter;
import com.omerozer.knit.Presenter;
import com.omerozer.knit.Updating;
import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;
import com.omerozer.sample.views.MainActivity;


/**
 * Created by omerozer on 2/2/18.
 */

@Presenter(MainActivity.class)
public class MainPresenter extends KnitPresenter<MainActivity> {


    @Override
    public void onCreate() {

    }

    @Override
    public void onViewApplied(Object viewObject) {
        requestData("testN","HOE ");
    }

    @Override
    public void onCurrentViewReleased() {

    }

    @Override
    public void handle(ViewEventPool eventPool, ViewEventEnv eventEnv, InternalModel modelManager) {
        if(eventEnv.getTag().equals("button")){
            getView().recMes("BUTTON PRESSED");
        }
        eventPool.pool(eventEnv);
    }

    @Updating("test")
    void updateData1(String data){

    }

    @Updating("testN")
    void updateData2(String data){
        getView().recMes(data);
    }

}
