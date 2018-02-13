package com.omerozer.knit;

import android.content.Context;

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

    public static void init(Context context) {
        knitUtilsLoader  = new KnitUtilsLoader();
        knitMemoryManager = new KnitMemoryManager(context);
        knitAsyncTaskHandler = new KnitAsyncTaskHandler();
        modelManager = new ModelManager(Knit.class,knitUtilsLoader,knitAsyncTaskHandler);
        knitClassLoader = new KnitClassLoader(Knit.class,modelManager);
        presenterInstancePool = new PresenterInstancePool(knitClassLoader,knitMemoryManager,modelManager);
    }

    public static void show(Object viewObject) {
        presenterInstancePool.applyPresenterInstanceToView(viewObject);
    }

    public static void dismiss(Object viewObject){
        presenterInstancePool.getPresenterInstanceForView(viewObject).onCurrentViewReleased();
    }

    static InternalPresenter findPresenterForView(Object viewObject){
        return presenterInstancePool.getPresenterInstanceForView(viewObject);
    }

    static InternalPresenter findPresenterForParent(Object viewObject){
        return presenterInstancePool.getPresenterInstanceForParent(viewObject);
    }


}
