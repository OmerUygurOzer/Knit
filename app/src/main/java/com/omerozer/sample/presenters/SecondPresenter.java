package com.omerozer.sample.presenters;

import android.os.Bundle;
import android.util.Log;

import com.omerozer.knit.KnitPresenter;
import com.omerozer.knit.KnitResponse;
import com.omerozer.knit.Presenter;
import com.omerozer.knit.Updating;
import com.omerozer.knit.schedulers.KnitSchedulers;
import com.omerozer.sample.datatype.StringWrapper;
import com.omerozer.sample.views.SecondActivity;
import com.omerozer.sample.views.SecondActivityContract;

import java.util.List;

/**
 * Created by omerozer on 2/6/18.
 */

@Presenter(SecondActivity.class)
public class SecondPresenter extends KnitPresenter<SecondActivityContract> {

    @Override
    public void onViewApplied(Object viewObject,Bundle bundle){
        super.onViewApplied(viewObject,bundle);
        request("umbrella", KnitSchedulers.IO,KnitSchedulers.MAIN);
        request("umbrella", KnitSchedulers.IO,KnitSchedulers.MAIN);
        request("umbrella", KnitSchedulers.IO,KnitSchedulers.MAIN);
        request("umbrella", KnitSchedulers.IO,KnitSchedulers.MAIN);
        request("umbrella", KnitSchedulers.IO,KnitSchedulers.MAIN);
        request("Ttest", KnitSchedulers.IO,KnitSchedulers.MAIN);
    }

    @Override
    public void onCurrentViewReleased() {
        super.onCurrentViewReleased();
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

    @Updating("umbrella")
    void updateData2(KnitResponse<String> data){
        getContract().recMes(data.getBody());
    }

    @Updating("Ttest")
    void updateDatat2(KnitResponse<List<StringWrapper>> data){
        getContract().recMes(data.getBody().get(0).string);
    }
}
