package com.omerozer.knit;

import com.omerozer.knit.schedulers.KnitSchedulers;

/**
 * Created by bo_om on 3/12/2018.
 */

public class TestSingleton_Model extends InternalModel {

    @Override
    public void request(String data, KnitSchedulers runOn, KnitSchedulers consumeOn, InternalPresenter presenter, Object... params) {

    }

    @Override
    public <T> KnitResponse<T> requestImmediately(String data, Object... params) {
        return null;
    }

    @Override
    public void input(String data, Object... params) {

    }

    @Override
    public KnitModel getParent() {
        return null;
    }

    @Override
    public String[] getHandledValues() {
        return new String[0];
    }
}
