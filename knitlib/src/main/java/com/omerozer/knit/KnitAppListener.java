package com.omerozer.knit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by omerozer on 2/18/18.
 */

public class KnitAppListener implements Application.ActivityLifecycleCallbacks {

    private Knit knit;

    private FragmentManager.FragmentLifecycleCallbacks supportFragmentCallbacks;
    private android.app.FragmentManager.FragmentLifecycleCallbacks oFragmentCallbacks;

    private Class<? extends Activity> topType;
    private WeakReference<Activity> topActivityRef;
    private int orientation;

    KnitAppListener(Knit knit) {
        this.knit = knit;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if(topExists() && orientationChanged(activity)){
            knit.initViewDependencies(activity);
            return;
        }

        registerTopActivity(activity);
        attachFragmentListeners(activity);

    }

    private boolean topExists(){
        return topActivityRef!=null;
    }

    private void registerTopActivity(Activity activity){
        assignAsTop(activity);
        knit.initViewDependencies(activity);
    }

    private boolean orientationChanged(Activity activity){
        return this.orientation != activity.getResources().getConfiguration().orientation && topType.equals(activity.getClass());
    }

    private void assignAsTop(Activity activity){
        this.topActivityRef = new WeakReference<Activity>(activity);
        this.topType = activity.getClass();
        this.orientation = activity.getResources().getConfiguration().orientation;
    }

    @SuppressLint("NewApi")
    private void attachFragmentListeners(Activity activity){
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(
                            getSupportFragmentCallbacks(), true);
        } else {
            activity.getFragmentManager().registerFragmentLifecycleCallbacks(
                    getoFragmentCallbacks(), true);
        }
    }


    @Override
    public void onActivityStarted(Activity activity) {
        knit.findPresenterForView(activity).onViewStart();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        knit.findPresenterForView(activity).onViewResume();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        knit.findPresenterForView(activity).onViewPause();
    }

    @Override
    public void onActivityStopped(Activity activity) {
       knit.findPresenterForView(activity).onViewStop();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if(activity.isFinishing()){
            knit.destoryComponent(activity);
            return;
        }
    }

    public FragmentManager.FragmentLifecycleCallbacks getSupportFragmentCallbacks() {
        if (supportFragmentCallbacks == null) {
            supportFragmentCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
                @Override
                public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v,
                        Bundle savedInstanceState) {
                    super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                    knit.initViewDependencies(f);
                }

                @Override
                public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                    super.onFragmentDestroyed(fm, f);
                    knit.destoryComponent(f);
                }
            };
        }
        return supportFragmentCallbacks;
    }

    @SuppressLint("NewApi")
    public android.app.FragmentManager.FragmentLifecycleCallbacks getoFragmentCallbacks() {
        if (oFragmentCallbacks == null) {
            oFragmentCallbacks = new android.app.FragmentManager.FragmentLifecycleCallbacks() {
                @Override
                public void onFragmentViewCreated(android.app.FragmentManager fm,
                        android.app.Fragment f, View v,
                        Bundle savedInstanceState) {
                    super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                    knit.initViewDependencies(f);
                }

                @Override
                public void onFragmentDestroyed(android.app.FragmentManager fm,
                        android.app.Fragment f) {
                    super.onFragmentDestroyed(fm, f);
                    knit.destoryComponent(f);
                }
            };
        }
        return oFragmentCallbacks;
    }


}
