package com.omerozer.sample.presenters;

import com.omerozer.knit.Mutator;
import com.omerozer.knit.Presenter;
import com.omerozer.knit.Seed;
import com.omerozer.knit.mutators.Mutator1;
import com.omerozer.sample.views.SecondActivity;

/**
 * Created by omerozer on 2/6/18.
 */

@Presenter(SecondActivity.class)
public class SecondPresenter {

    @Seed("firstName")
    String firstName = "Omer";

    @Seed("rand")
    String rand;

    @Seed("key")
    String key = "Key";

    @Seed("age")
    int age = 28;

    @Mutator("age")
    Mutator1 mutateTestInteger = new Mutator1<Integer,Integer>() {
        @Override
        public Integer mutate(Integer source) {
            return source + 1;
        }
    };

}
