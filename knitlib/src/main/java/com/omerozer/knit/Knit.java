package com.omerozer.knit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import java.lang.ref.WeakReference;
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

    private static KnitClassLoader knitClassLoader;

    private static KnitUtilsLoader knitUtilsLoader;

    private static Map<Class<?>, Bundle> navigatorDataMap = new LinkedHashMap<>();

    private static WeakReference<? extends Activity> currentTopActivity;

    public static void init(Context context) {
        knitUtilsLoader = new KnitUtilsLoader();
        knitMemoryManager = new KnitMemoryManager(context);
        knitAsyncTaskHandler = new KnitAsyncTaskHandler();
        modelManager = new ModelManager(Knit.class, knitUtilsLoader, knitAsyncTaskHandler);
        knitClassLoader = new KnitClassLoader(Knit.class, modelManager);
        presenterInstancePool = new PresenterInstancePool(knitClassLoader, knitMemoryManager, modelManager);
    }

    public static void show(Object viewObject) {
        if(viewObject instanceof Activity){KnitNavigator.init((Activity)viewObject);}
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


}
