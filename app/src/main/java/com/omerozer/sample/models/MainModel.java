package com.omerozer.sample.models;

import com.omerozer.knit.Generates;
import com.omerozer.knit.GeneratesAsync;
import com.omerozer.knit.Model;
import com.omerozer.knit.generators.Generator0;
import com.omerozer.knit.generators.Generator1;

/**
 * Created by omerozer on 2/3/18.
 */

@Model
public class MainModel {


    @Generates("test")
    Generator0<String> generateTestString = new Generator0<String>() {
        @Override
        public String generate() {
            return "TEST STRING";
        }
    };

    @GeneratesAsync("testN")
    Generator1<String,String> generateTestTwoParams = new Generator1<String, String>() {
        @Override
        public String generate(String generate) {
            return generate+"YAS";
        }
    };



}
