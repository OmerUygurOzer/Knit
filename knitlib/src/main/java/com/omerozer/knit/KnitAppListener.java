package com.omerozer.knit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by omerozer on 2/18/18.
 */

public final class KnitAppListener implements Application.ActivityLifecycleCallbacks {

    private Knit knit;

    private FragmentManager.FragmentLifecycleCallbacks supportFragmentCallbacks;
    private android.app.FragmentManager.FragmentLifecycleCallbacks oFragmentCallbacks;

    KnitAppListener(Knit knit) {
        this.knit = knit;
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
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
        knit.initViewDependencies(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        knit.releaseViewFromComponent(activity);
    }

    private FragmentManager.FragmentLifecycleCallbacks getSupportFragmentCallbacks() {
        if (supportFragmentCallbacks == null) {
            supportFragmentCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
                @Override
                public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v,
                        Bundle savedInstanceState) {
                    super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                }

                @Override
                public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                    super.onFragmentDestroyed(fm, f);
                }
            };
        }
        return supportFragmentCallbacks;
    }

    @SuppressLint("NewApi")
    private android.app.FragmentManager.FragmentLifecycleCallbacks getoFragmentCallbacks() {
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
                    knit.releaseViewFromComponent(f);
                }
            };
        }
        return oFragmentCallbacks;
    }


}
