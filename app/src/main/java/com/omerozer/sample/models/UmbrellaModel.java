package com.omerozer.sample.models;

import com.omerozer.knit.Collects;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.Model;
import com.omerozer.knit.collectors.Collector0;
import com.omerozer.knit.collectors.Submitter;
import com.omerozer.knit.generators.Callback;

/**
 * Created by omerozer on 2/14/18.
 */
@Model
public class UmbrellaModel extends KnitModel {


    @Override
    public void onCreate() {

    }

    @Collects("umbrella")
    Collector0<String> collector = new Collector0<String>() {
        @Override
        public void collect(final Submitter<String> submitter) {
            request("test", new Callback<String>() {
                @Override
                public void response(final String response) {
                    request("testN", new Callback<String>() {
                        @Override
                        public void response(String response1) {
                            submitter.submit(response+" "+response1);
                        }
                    },"BOO");
                }
            });
        }
    };

}
