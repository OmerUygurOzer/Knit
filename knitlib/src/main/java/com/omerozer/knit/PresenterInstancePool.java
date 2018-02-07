package com.omerozer.knit;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/1/18.
 */

public class PresenterInstancePool {

    private KnitClassLoader knitClassLoader;

    private KnitMemoryManager knitMemoryManager;

    private Map<Class<?>, KnitPresenter> instanceMap;

    private ModelManager modelManager;

    PresenterInstancePool(KnitClassLoader knitClassLoader,KnitMemoryManager knitMemoryManager,ModelManager modelManager) {
        this.knitClassLoader = knitClassLoader;
        this.knitMemoryManager = knitMemoryManager;
        this.instanceMap = new HashMap<>();
        this.modelManager = modelManager;
    }

    <T> KnitPresenter getPresenterInstance(Object object) {
        Class<?> clazz = object.getClass();
        if (instanceMap.containsKey(clazz)) {
            handleLoadState(instanceMap.get(clazz));
            return instanceMap.get(clazz);
        }
        KnitPresenter presenterInstance = knitClassLoader.createPresentersInstance(object,modelManager);
        handleLoadState(presenterInstance);
        knitMemoryManager.registerInstance(presenterInstance);
        return presenterInstance;
    }

    void handleLoadState(KnitPresenter knitPresenter){
        if(knitPresenter.shouldLoad()){
            knitPresenter.load();
        }
    }

}
