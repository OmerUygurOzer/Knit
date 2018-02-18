package com.omerozer.knit.classloaders;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.KnitNavigator;
import com.omerozer.knit.ModelMapInterface;
import com.omerozer.knit.ViewToPresenterMapInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by omerozer on 2/12/18.
 */

public class KnitUtilsLoader {

    private static final String KNIT_VIEW_PRESENTER_MAP = "com.omerozer.knit.ViewToPresenterMap";
    private static final String KNIT_MODEL_MAP = "com.omerozer.knit.ModelMap";

    private Constructor<?> getConstructorForModelMap(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();

        try {
            Class<?> presenter = classLoader.loadClass(KNIT_MODEL_MAP);
            Constructor<?> constructor = presenter.getConstructor();

            return constructor;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ModelMapInterface getModelMap(Class<?> clazz){
        try {
            return (ModelMapInterface)getConstructorForModelMap(clazz).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Constructor<?> getViewToPresenterConstructor(Class<?> clazz) {

        ClassLoader classLoader = clazz.getClassLoader();

        try {
            Class<?> presenterMapClazz = classLoader.loadClass(KNIT_VIEW_PRESENTER_MAP);
            Constructor<?> constructor = presenterMapClazz.getConstructor(KnitNavigator.class,InternalModel.class);

            return constructor;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ViewToPresenterMapInterface getViewToPresenterMap(Class<?> clazz){
        try {
            return (ViewToPresenterMapInterface)getViewToPresenterConstructor(clazz).newInstance();
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
