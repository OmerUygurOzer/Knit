package com.omerozer.knit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.omerozer.knit.components.KnitMemoryManager;
import com.omerozer.knit.components.ModelManager;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/1/18.
 */

public class Knit {

    private static PresenterInstancePool presenterInstancePool;

    private static KnitMemoryManager knitMemoryManager;

    private static KnitAsyncTaskHandler knitAsyncTaskHandler;

    private static ModelManager modelManager;

    private static Map<Class<?>, Bundle> navigatorDataMap = new LinkedHashMap<>();

    private static KnitNavigator navigator;

    public static void init(Context context) {
        //knitMemoryManager = new KnitMemoryManager(context);
        navigator = KnitNavigator.getInstance();
        knitAsyncTaskHandler = new KnitAsyncTaskHandler();
        //modelManager = new ModelManager(Knit.class, knitUtilsLoader, knitAsyncTaskHandler);
        //knitClassLoader = new KnitClassLoader(Knit.class, navigator,modelManager);
        //presenterInstancePool = new PresenterInstancePool(knitClassLoader, knitMemoryManager, navigator,modelManager);
    }

    public static void show(Object viewObject) {
        if(viewObject instanceof Activity){navigator.setContext ((Activity)viewObject);}
        Class<?> target = viewObject.getClass();
        presenterInstancePool.applyPresenterInstanceToView(viewObject,
                navigatorDataMap.containsKey(target) ? navigatorDataMap.get(target) : null);
    }

    public static void dismiss(Object viewObject) {
        presenterInstancePool.getPresenterInstanceForView(viewObject).onCurrentViewReleased();
    }

    static InternalPresenter findPresenterForView(Object viewObject) {
        return presenterInstancePool.getPresenterInstanceForView(viewObject);
    }

    static InternalPresenter findPresenterForParent(Object viewObject) {
        return presenterInstancePool.getPresenterInstanceForParent(viewObject);
    }

    static void setDataForNavigation(KnitNavigator.Navitator navigator, Bundle bundle) {
        if (bundle != null) {
            navigatorDataMap.put(navigator.getTarget(), bundle);
        }
    }

    static InternalModel getModelManager(){
        return modelManager;
    }


}
