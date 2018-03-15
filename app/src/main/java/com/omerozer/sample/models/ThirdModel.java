package com.omerozer.sample.models;

import android.util.Log;

import com.omerozer.knit.Generates;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.KnitResponse;
import com.omerozer.knit.Model;
import com.omerozer.knit.generators.Generator0;
import com.omerozer.sample.datatype.StringWrapper;

import java.util.Arrays;
import java.util.List;

/**
 * Created by omerozer on 3/1/18.
 */

@Model
public class ThirdModel extends KnitModel {


    @Generates("Ttest")
    public Generator0<List<StringWrapper>> generateTestString = new Generator0<List<StringWrapper>>() {

        @Override
        public KnitResponse<List<StringWrapper>> generate() {
            Log.d("KNIT_TEST","TEST CALL");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new KnitResponse<>(Arrays.asList(new StringWrapper("TEEEEST")));
        }
    };

}
