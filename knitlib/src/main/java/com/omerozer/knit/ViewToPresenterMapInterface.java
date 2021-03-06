package com.omerozer.knit;

import java.util.List;

/**
 * Created by omerozer on 2/6/18.
 */

public interface ViewToPresenterMapInterface {
    Class getPresenterClassForView(Class viewClass);
    Class getPresenterClassForPresenter(Class parentClass);
    List<Class<?>> getAllViews();
    List<String> getUpdatingValues(Class clazz);
}
