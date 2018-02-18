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

    private Map<Class<?>, Constructor<?>> cache;

    private KnitNavigator navigator;

    private InternalModel modelManager;

    public KnitPresenterLoader(KnitNavigator navigator, InternalModel modelManager) {
        this.navigator = navigator;
        this.modelManager = modelManager;
        this.cache = new HashMap<>();
    }

    public InternalPresenter loadPresenter(Class<?> presenterClass) {
        try {
            return (InternalPresenter) findConstructorForPresenter(presenterClass).newInstance(
                    navigator, modelManager);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Constructor<?> findConstructorForPresenter(Class<?> presenterClazz) {
        if (cache.containsKey(presenterClazz)) {
            return cache.get(presenterClazz);
        }
        try {
            Constructor<?> constructor = presenterClazz.getConstructor(Object.class,
                    KnitNavigator.class, InternalModel.class);
            cache.put(presenterClazz, constructor);
            return constructor;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
