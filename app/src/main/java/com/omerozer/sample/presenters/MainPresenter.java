package com.omerozer.sample.presenters;

import android.util.Log;

import com.omerozer.knit.*;
import com.omerozer.knit.Mutator;
import com.omerozer.knit.mutators.*;
import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;
import com.omerozer.knit.viewevents.handlers.EventHandler;
import com.omerozer.sample.TestObject;
import com.omerozer.sample.views.MainActivity;


/**
 * Created by omerozer on 2/2/18.
 */

@Presenter(MainActivity.class)
public class MainPresenter {

    @Seed("fullName")
    String fullName;

    @Seed("firstName")
    String firstName;

    @Seed("surname")
    String lastName;

    @Seed("key")
    String key;

    @Seed("age")
    int age;

    @Seed("year")
    int year;

    @Seed("obj")
    TestObject object;

    @Seed("rand")
    String rand;

    @Mutator(
            value = "fullName",
            params = {"firstName","surname"}
    )
    Mutator2<String,String,String> mutateTestString = new Mutator2<String, String, String>() {
        @Override
        public String mutate(String param1,String param2) {
            Log.d("KNIT_TEST","Mutating fullName");
            return param1 + " " + param2;
        }
    };

    @Mutator("age")
    Mutator1 mutateTestInteger = new Mutator1<Integer,Integer>() {
        @Override
        public Integer mutate(Integer source) {
            return source + 1;
        }
    };

    @Mutator("obj")
    Mutator1<TestObject,TestObject> mutateObject = new Mutator1<TestObject, TestObject>() {
        @Override
        public TestObject mutate(TestObject source) {
            return null;
        }
    };

    @ViewEventHandler("clickButton")
    EventHandler handler = new EventHandler() {
        @Override
        public void handle(ViewEventPool eventPool, ViewEventEnv eventEnv, KnitModel modelManager) {
           // modelManager.request();
            eventPool.pool(eventEnv);
        }
    };



}
