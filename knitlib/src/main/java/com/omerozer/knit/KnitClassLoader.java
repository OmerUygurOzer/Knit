package com.omerozer.knit;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/6/18.
 */

public class KnitClassLoader {

    private static final String MODEL_POSTFIX = "_Model";

    private static final String KNIT_MODEL_MAP = "com.omerozer.knit.ModelMap";

    private static final String KNIT_VIEW_PRESENTER_MAP = "com.omerozer.knit.ViewToPresenterMap";

    private Map<Class<?>,Constructor<?>> CACHE = new LinkedHashMap<>();

    private ViewToPresenterMapInterface viewToPresenterMap;

    public KnitClassLoader(Class<?> clazz){
        viewToPresenterMap = getViewToPresenterMap(clazz);
    }

    private Constructor<?> getConstructorForPresenter(Class<?> clazz) {

        ClassLoader classLoader = clazz.getClassLoader();

        String target = viewToPresenterMap.getPresenterClass(clazz.getCanonicalName()).trim();

        try {
            Class<?> presenter = classLoader.loadClass(target);
            Constructor<?> constructor = presenter.getConstructor(Object.class,KnitModel.class);

            return constructor;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }


    KnitPresenter createPresentersInstance(Object parent, KnitModel modelManager){
        return viewToPresenterMap.createPresenterForView(parent,modelManager);
    }

    private Constructor<?> getConstructorForModel(Class<?> clazz) {

        ClassLoader classLoader = clazz.getClassLoader();

        try {
            Class<?> presenter = classLoader.loadClass(clazz.getCanonicalName()+MODEL_POSTFIX);
            Constructor<?> constructor = presenter.getConstructor(Object.class,KnitAsyncTaskHandler.class);

            return constructor;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    KnitModel createModelInstance(Class<?> clazz,Object parent, KnitAsyncTaskHandler asyncTaskHandler){
        try {
            return (KnitModel) getConstructorForPresenter(clazz).newInstance(parent,asyncTaskHandler);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

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

     ModelMapInterface getModelMap(Class<?> clazz){
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
            Class<?> presenter = classLoader.loadClass(KNIT_VIEW_PRESENTER_MAP);
            Constructor<?> constructor = presenter.getConstructor();

            return constructor;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ViewToPresenterMapInterface getViewToPresenterMap(Class<?> clazz){
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
