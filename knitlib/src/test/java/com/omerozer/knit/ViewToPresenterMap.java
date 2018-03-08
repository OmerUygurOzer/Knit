package com.omerozer.knit;

import java.util.List;

/**
 * Created by omerozer on 3/8/18.
 */

public class ViewToPresenterMap implements ViewToPresenterMapInterface {
    @Override
    public Class getPresenterClassForView(Class viewClass) {
        return null;
    }

    @Override
    public Class getPresenterClassForPresenter(Class parentClass) {
        return null;
    }

    @Override
    public List<Class<?>> getAllViews() {
        return null;
    }

    @Override
    public List<String> getUpdatingValues(Class clazz) {
        return null;
    }
}
