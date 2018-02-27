package com.omerozer.sample.models;

import android.util.Log;

import com.omerozer.knit.Generates;
import com.omerozer.knit.GeneratesAsync;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.KnitResponse;
import com.omerozer.knit.KnitSchedulers;
import com.omerozer.knit.Model;
import com.omerozer.knit.generators.Generator1;

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

    @Generates(value = "testN",runOn = KnitSchedulers.IO,consumeOn = KnitSchedulers.MAIN)
    Generator1<String,String> generateTestTwoParams = new Generator1<String, String>() {
        @Override
        public KnitResponse<String> generate(String generate) {
            return new KnitResponse<>(generate+"YAAAH");
        }
    };
}
