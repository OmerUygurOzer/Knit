package com.omerozer.sample.models;

import com.omerozer.knit.GeneratesAsync;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.Model;
import com.omerozer.knit.generators.Generator1;

/**
 * Created by omerozer on 2/6/18.
 */

@Model
public class ModelTwo extends KnitModel {
    @Override
    public void onCreate() {

    }

    @GeneratesAsync("testN")
    Generator1<String,String> generateTestTwoParams = new Generator1<String, String>() {
        @Override
        public String generate(String generate) {
            return generate+"YAAAH";
        }
    };
}
