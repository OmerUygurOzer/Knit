package com.omerozer.knit.classloaders;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.KnitAsyncTaskHandler;
import com.omerozer.knit.KnitModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/16/18.
 */

public class KnitModelLoader {

    private Map<Class<?>, Constructor<?>> cache;

    private KnitAsyncTaskHandler asyncTaskHandler;

    public KnitModelLoader(KnitAsyncTaskHandler asyncTaskHandler) {
        this.cache = new HashMap<>();
        this.asyncTaskHandler = asyncTaskHandler;
    }

    public InternalModel loadModel(Class<?> modelClazz) {
        try {
            return (InternalModel) findConstructorForModel(modelClazz).newInstance(asyncTaskHandler);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Constructor<?> findConstructorForModel(Class<?> modelClazz) {

        if(cache.containsKey(modelClazz)){
            return cache.get(modelClazz);
        }

        try {
            Constructor<?> constructor = modelClazz.getConstructor(KnitAsyncTaskHandler.class);
            cache.put(modelClazz,constructor);
            return constructor;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
