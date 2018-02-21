package com.omerozer.sample.presenters;

import android.os.Bundle;
import android.util.Log;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.KnitPresenter;
import com.omerozer.knit.KnitResponse;
import com.omerozer.knit.Presenter;
import com.omerozer.knit.Updating;
import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;
import com.omerozer.sample.views.SecondActivity;
import com.omerozer.sample.views.SecondActivityContract;

/**
 * Created by omerozer on 2/6/18.
 */

@Presenter(SecondActivity.class)
public class SecondPresenter extends KnitPresenter<SecondActivityContract> {

    @Override
    public void onViewApplied(Object viewObject,Bundle bundle){
        super.onViewApplied(viewObject,bundle);
        requestData("testN","PARAM_SENT");
    }

    @Override
    public void onCurrentViewReleased() {
        super.onCurrentViewReleased();
        destroyComponent();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KNIT_TEST","PRESENTER TWO CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("KNIT_TEST","PRESENTER TWO DESTROYED");
    }

    @Updating("testN")
    void updateData2(KnitResponse<String> data){
        getContract().recMes(data.getBody());
    }
}
