package com.omerozer.sample.models;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.omerozer.knit.Generates;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.KnitResponse;
import com.omerozer.knit.schedulers.KnitSchedulers;
import com.omerozer.knit.Model;
import com.omerozer.knit.generators.Generator1;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by omerozer on 2/6/18.
 */

@Model
public class ModelTwo extends KnitModel {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KNIT_TEST","MODELTWO CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("KNIT_TEST","MODELTWO DESTROYED");
    }

    @Generates("testN")
    Generator1<String,String> generateTestTwoParams = new Generator1<String, String>() {
        @Override
        public KnitResponse<String> generate(String generate) {
            Log.d("KNIT_TEST","TEST_N CALL");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new KnitResponse<>(generate+"YAAAH"+Integer.toString(123654));
        }
    };
}
