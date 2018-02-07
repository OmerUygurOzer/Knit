package com.omerozer.sample.models;

import com.omerozer.knit.Generates;
import com.omerozer.knit.GeneratesAsync;
import com.omerozer.knit.Model;
import com.omerozer.knit.generators.Generator0;
import com.omerozer.knit.generators.Generator1;
import com.omerozer.knit.generators.Generator2;

/**
 * Created by omerozer on 2/3/18.
 */

@Model
public class MainModel {

    @Generates({"firstName","omersname"})
    Generator0<String> generateFirstName = new Generator0<String>() {
        @Override
        public String generate() {
            return "Omer";
        }
    };

    @Generates("surname")
    Generator0<String> generateLastName = new Generator0<String>() {
        @Override
        public String generate() {
            return "Ozer";
        }
    };

    @GeneratesAsync("age")
    Generator0<Integer> generateAge = new Generator0<Integer>() {
        @Override
        public Integer generate() {
            return 28;
        }
    };

    @GeneratesAsync(value = "rand",takes = {"firstName","key"})
    Generator2<String,String,String> generateStringFromString = new Generator2<String, String,String>() {

        @Override
        public String generate(String param1, String param2) {
            return param1 + " HUA"+ param2;
        }
    };

    @Generates(value = "test")
    Generator0<String> generateTest = new Generator0<String>() {
        @Override
        public String generate() {
            return "Test";
        }
    };

}
