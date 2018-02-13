package com.omerozer.knit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/6/18.
 */

public class KnitClassLoader {

    private static final String MODEL_POSTFIX = "_Model";

    private static final String KNIT_VIEW_PRESENTER_MAP = "com.omerozer.knit.ViewToPresenterMap";

    private Map<Class<?>,Constructor<?>> CACHE = new LinkedHashMap<>();

    private ViewToPresenterMapInterface viewToPresenterMap;

    public KnitClassLoader(Class<?> clazz,InternalModel modelManager){
        this.viewToPresenterMap = getViewToPresenterMap(clazz,modelManager);
    }

    private Constructor<?> getConstructorForPresenter(Class<?> clazz) {

        ClassLoader classLoader = clazz.getClassLoader();

        String target = viewToPresenterMap.getPresenterClass(clazz.getCanonicalName()).trim();

        try {
            Class<?> presenter = classLoader.loadClass(target);
            Constructor<?> constructor = presenter.getConstructor(Object.class,InternalModel.class);

            return constructor;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }


    InternalPresenter createPresenterInstanceForView(Object parent, InternalModel modelManager){
        return viewToPresenterMap.createPresenterForView(parent,modelManager);
    }

    InternalPresenter createPresenterInstanceForParent(Object parent){
        return viewToPresenterMap.getPresenterForParent(parent);
    }



    private Constructor<?> getViewToPresenterConstructor(Class<?> clazz) {

        ClassLoader classLoader = clazz.getClassLoader();

        try {
            Class<?> presenter = classLoader.loadClass(KNIT_VIEW_PRESENTER_MAP);
            Constructor<?> constructor = presenter.getConstructor(InternalModel.class);

            return constructor;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ViewToPresenterMapInterface getViewToPresenterMap(Class<?> clazz,InternalModel modelManager){
        try {
            return (ViewToPresenterMapInterface)getViewToPresenterConstructor(clazz).newInstance(modelManager);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


}
