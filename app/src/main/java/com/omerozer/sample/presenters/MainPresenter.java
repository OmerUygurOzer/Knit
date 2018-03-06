package com.omerozer.sample.presenters;

import static com.omerozer.sample.views.MainActivity.BUTTON_CLICK;

import android.os.Bundle;
import android.util.Log;

import com.omerozer.knit.KnitPresenter;
import com.omerozer.knit.Presenter;
import com.omerozer.knit.ViewEvent;
import com.omerozer.knit.viewevents.ViewEventEnv;
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

    @ViewEvent(BUTTON_CLICK)
    public void handle(ViewEventEnv eventEnv) {
        if(eventEnv.getTag().equals("button")){

            getNavigator()
                    .toActivity()
                    .target(SecondActivity.class)
                    .go();
        }

    }



}
