package com.omerozer.knit;

import android.os.Bundle;

import com.omerozer.knit.components.KnitMemoryManager;
import com.omerozer.knit.components.ModelManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/1/18.
 */

public class PresenterInstancePool {


    private KnitMemoryManager knitMemoryManager;

    private Map<Class<?>, InternalPresenter> instanceMap;

    private ModelManager modelManager;

    private KnitNavigator navigator;

    PresenterInstancePool( KnitMemoryManager knitMemoryManager,
            KnitNavigator navigator, ModelManager modelManager) {
        this.knitMemoryManager = knitMemoryManager;
        this.instanceMap = new HashMap<>();
        this.modelManager = modelManager;
        this.navigator = navigator;
    }

    void applyPresenterInstanceToView(Object viewObject, Bundle data) {
        InternalPresenter presenter = getPresenterInstanceForView(viewObject);
        presenter.onViewApplied(viewObject, data);
        handleLoadState(presenter);
    }


    InternalPresenter getPresenterInstanceForView(Object object) {
        Class<?> clazz = object.getClass();
        if (instanceMap.containsKey(clazz)) {
            return instanceMap.get(clazz);
        }
        InternalPresenter presenterInstance =null;// = knitClassLoader.createPresenterInstanceForView(object,
                //navigator, modelManager);
        knitMemoryManager.registerInstance(presenterInstance);
        presenterInstance.onCreate();
        return presenterInstance;
    }

    InternalPresenter getPresenterInstanceForParent(Object object) {
        InternalPresenter presenterInstance = null;// knitClassLoader.createPresenterInstanceForParent(object);
        knitMemoryManager.registerInstance(presenterInstance);
        return presenterInstance;
    }

    void handleLoadState(InternalPresenter knitPresenter) {
        if (knitPresenter.shouldLoad()) {
            knitPresenter.onLoad();
        }
    }

}
