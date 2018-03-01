package com.omerozer.knit.classloaders;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.Knit;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.ModelMapInterface;
import com.omerozer.knit.schedulers.SchedulerProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/16/18.
 */

public class KnitModelLoader {

    private Map<Class<?>, Constructor<?>> cache;

    private SchedulerProvider schedulerProvider;

    private ModelMapInterface modelMap;

    public KnitModelLoader(SchedulerProvider schedulerProvider) {
        this.cache = new HashMap<>();
        this.schedulerProvider = schedulerProvider;
        modelMap = new KnitUtilsLoader().getModelMap(Knit.class);
    }

    public InternalModel loadModel(Class<?> modelClazz) {
        try {
            return (InternalModel) findConstructorForModel(modelClazz).newInstance(schedulerProvider);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class<? extends InternalModel> getModelForModel(Class<? extends KnitModel> clazz){
        return modelMap.getModelClassForModel(clazz);
    }

    private Constructor<?> findConstructorForModel(Class<?> modelClazz) {

        if(cache.containsKey(modelClazz)){
            return cache.get(modelClazz);
        }

        try {
            Constructor<?> constructor = modelClazz.getConstructor(SchedulerProvider.class);
            cache.put(modelClazz,constructor);
            return constructor;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
