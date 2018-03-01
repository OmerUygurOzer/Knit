package com.omerozer.sample.presenters;

import android.os.Bundle;
import android.util.Log;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.KnitPresenter;
import com.omerozer.knit.KnitResponse;
import com.omerozer.knit.Presenter;
import com.omerozer.knit.Updating;
import com.omerozer.knit.schedulers.KnitSchedulers;
import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;
import com.omerozer.sample.views.MainActivity;
import com.omerozer.sample.views.MainActivityContract;
import com.omerozer.sample.views.SecondActivity;


/**
 * Created by omerozer on 2/2/18.
 */

@Presenter(MainActivity.class)
public class MainPresenter extends KnitPresenter<MainActivityContract> {

    @Override
    public void onViewApplied(Object viewObject,Bundle data){
        super.onViewApplied(viewObject,data);

    }

    @Override
    public void onCurrentViewReleased() {
        super.onCurrentViewReleased();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KNIT_TEST","PRESENTER MAIN CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("KNIT_TEST","PRESENTER MAIN DESTROYED");
    }

    @Override
    public void handle(ViewEventPool eventPool, ViewEventEnv eventEnv, InternalModel modelManager) {
        if(eventEnv.getTag().equals("button")){

           // getContract().recMes("BUTTON PRESSED");

           // request("umbrella");

            getNavigator()
                    .toActivity()
                    .target(SecondActivity.class)
                    .go();

        }

        eventPool.pool(eventEnv);
    }

//    @Updating("umbrella")
//    public void updateData1(KnitResponse<String> data){
//        getContract().recMes(data.getBody());
//    }

//    @Updating("testN")
//    public void updateData1N(KnitResponse<String> data){
//        getContract().recMes(data.getBody());
//    }


}
