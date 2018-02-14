package com.omerozer.sample.models;

import com.omerozer.knit.Generates;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.Model;
import com.omerozer.knit.generators.Generator0;

/**
 * Created by omerozer on 2/3/18.
 */

@Model
public class MainModel extends KnitModel {

    @Override
    public void onCreate() {

    }

    @Generates("test")
    Generator0<String> generateTestString = new Generator0<String>() {
        @Override
        public String generate() {



            return "TEST STRING";
        }
    };







}
