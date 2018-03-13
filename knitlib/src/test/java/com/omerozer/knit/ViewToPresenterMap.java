package com.omerozer.knit;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Omer Ozer on 3/8/18.
 */

public class ViewToPresenterMap implements ViewToPresenterMapInterface {
    @Override
    public Class getPresenterClassForView(Class viewClass) {
        if(viewClass.equals(View1.class)){
            return TestPresenter_Presenter.class;
        }
        if(viewClass.equals(View2.class)){
            return TestPresenter2_Presenter.class;
        }
        return null;
    }

    @Override
    public Class getPresenterClassForPresenter(Class parentClass) {
        if(parentClass.equals(TestPresenter.class)){
            return TestPresenter_Presenter.class;
        }
        if(parentClass.equals(TestPresenter2.class)){
            return TestPresenter2_Presenter.class;
        }
        return null;
    }

    @Override
    public List<Class<?>> getAllViews() {
        return Arrays.asList(View1.class, View2.class);
    }

    @Override
    public List<String> getUpdatingValues(Class clazz) {
        if(clazz.equals(TestPresenter_Presenter.class)){
            return Arrays.asList("umb");
        }

        if(clazz.equals(TestPresenter2_Presenter.class)){
            return Arrays.asList();
        }

        return null;
    }
}
