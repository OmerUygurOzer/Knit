package com.omerozer.knit;

import java.util.List;

/**
 * Created by omerozer on 2/6/18.
 */

public interface ViewToPresenterMapInterface {
    Class getPresenterClassForView(Class viewObject);
    List<Class<?>> getAllViews();
    List<String> getUpdatingValues(Class clazz);
}
