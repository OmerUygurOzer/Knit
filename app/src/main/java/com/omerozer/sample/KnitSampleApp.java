package com.omerozer.sample;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.omerozer.knit.Knit;

/**
 * Created by omerozer on 2/6/18.
 */

public class KnitSampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Knit.init(this);
    }
}
