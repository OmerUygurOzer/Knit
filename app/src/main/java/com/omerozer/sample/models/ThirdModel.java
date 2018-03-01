package com.omerozer.sample.models;

import android.util.Log;

import com.omerozer.knit.Generates;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.KnitResponse;
import com.omerozer.knit.Model;
import com.omerozer.knit.generators.Generator0;

/**
 * Created by omerozer on 3/1/18.
 */

@Model
public class ThirdModel extends KnitModel {

    @Generates("Ttest")
    public Generator0<String> generateTestString = new Generator0<String>() {

        @Override
        public KnitResponse<String> generate() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("KNIT_TEST","TEST CALL");
            return new KnitResponse<>("TEEEESST STRING");
        }
    };

}
