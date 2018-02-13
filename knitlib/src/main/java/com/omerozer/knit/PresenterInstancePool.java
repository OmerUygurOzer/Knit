package com.omerozer.knit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/1/18.
 */

public class PresenterInstancePool {

    private KnitClassLoader knitClassLoader;

    private KnitMemoryManager knitMemoryManager;

    private Map<Class<?>, InternalPresenter> instanceMap;

    private ModelManager modelManager;

    PresenterInstancePool(KnitClassLoader knitClassLoader,KnitMemoryManager knitMemoryManager,ModelManager modelManager) {
        this.knitClassLoader = knitClassLoader;
        this.knitMemoryManager = knitMemoryManager;
        this.instanceMap = new HashMap<>();
        this.modelManager = modelManager;
    }

    void applyPresenterInstanceToView(Object viewObject){
        InternalPresenter presenter = getPresenterInstanceForView(viewObject);
        presenter.onViewApplied(viewObject);
        handleLoadState(presenter);
    }


    InternalPresenter getPresenterInstanceForView(Object object) {
        Class<?> clazz = object.getClass();
        if (instanceMap.containsKey(clazz)) {
            return instanceMap.get(clazz);
        }
        InternalPresenter presenterInstance = knitClassLoader.createPresenterInstanceForView(object,modelManager);
        knitMemoryManager.registerInstance(presenterInstance);
        return presenterInstance;
    }

    InternalPresenter getPresenterInstanceForParent(Object object) {
        InternalPresenter presenterInstance = knitClassLoader.createPresenterInstanceForParent(object);
        knitMemoryManager.registerInstance(presenterInstance);
        return presenterInstance;
    }

    void handleLoadState(InternalPresenter knitPresenter){
        if(knitPresenter.shouldLoad()){
            knitPresenter.load();
        }
    }

}
