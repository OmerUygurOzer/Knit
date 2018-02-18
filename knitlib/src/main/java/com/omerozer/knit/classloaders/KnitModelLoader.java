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
    
    private Map<Class<? extends KnitModel>,Constructor<?>> cache;
    
    private Class<?> base;
    
    private KnitAsyncTaskHandler asyncTaskHandler;
    
    public KnitModelLoader(Class<?> base, KnitAsyncTaskHandler asyncTaskHandler){
        this.cache = new HashMap<>();
        this.base = base;
        this.asyncTaskHandler = asyncTaskHandler;
    }
    
    public InternalModel loadModel(Object modelObject){
        try {
            return (InternalModel) findConstructorForModel(modelObject.getClass()).newInstance(modelObject,asyncTaskHandler);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Constructor<?> findConstructorForModel(Class<?> modelClazz){
        ClassLoader classLoader = base.getClassLoader();
        String name = modelClazz.getCanonicalName();
        Class<?> loadedModel = null;

        try {
            loadedModel = classLoader.loadClass(name);
            return loadedModel.getConstructor(Object.class,KnitAsyncTaskHandler.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
