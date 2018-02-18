package com.omerozer.knit.classloaders;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.InternalPresenter;
import com.omerozer.knit.KnitAsyncTaskHandler;
import com.omerozer.knit.KnitNavigator;
import com.omerozer.knit.ViewToPresenterMapInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/16/18.
 */

public class KnitPresenterLoader {


    private ViewToPresenterMapInterface viewToPresenterMap;

    private KnitNavigator navigator;

    private InternalModel modelManager;

    public KnitPresenterLoader(ViewToPresenterMapInterface viewToPresenterMap, KnitNavigator navigator, InternalModel modelManager) {
        this.viewToPresenterMap = viewToPresenterMap;
        this.navigator = navigator;
        this.modelManager = modelManager;
    }

    public InternalPresenter loadPresenterForView(Object presenterObject){
        try {
            return (InternalPresenter) findConstructorForPresenter(viewToPresenterMap.getPresenterClassForView(presenterObject.getClass())).newInstance(presenterObject,navigator,modelManager);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Constructor<?> findConstructorForPresenter(Class<?> presenterClazz){
        try {
            return   presenterClazz.getConstructor(Object.class,KnitNavigator.class,InternalModel.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
