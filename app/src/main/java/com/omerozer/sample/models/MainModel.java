package com.omerozer.sample.models;

import android.util.Log;

import com.omerozer.knit.Generates;
import com.omerozer.knit.InstanceType;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.KnitResponse;
import com.omerozer.knit.Model;
import com.omerozer.knit.generators.Generator0;
import com.omerozer.knit.generators.Generator1;

/**
 * Created by omerozer on 2/3/18.
 */

@Model(InstanceType.SINGLETON)
public class MainModel extends KnitModel {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KNIT_TEST","MAIN MODEL CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("KNIT_TEST","MAIN MODEL DESTROYED");
    }

    @Generates("test")
    public Generator0<String> generateTestString = new Generator0<String>() {

        @Override
        public KnitResponse<String> generate() {
            Log.d("KNIT_TEST","TEST CALL");
            return new KnitResponse<>("TEEEESST STRING");
        }
    };






}
