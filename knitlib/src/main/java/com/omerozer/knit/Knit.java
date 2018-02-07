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

    public static void init(Context context) {
        knitMemoryManager = new KnitMemoryManager(context);
        knitAsyncTaskHandler = new KnitAsyncTaskHandler();
        knitClassLoader = new KnitClassLoader(Knit.class);
        modelManager = new ModelManager(Knit.class,knitClassLoader,knitAsyncTaskHandler);
        presenterInstancePool = new PresenterInstancePool(knitClassLoader,knitMemoryManager,modelManager);
    }

    public static void show(Object viewObject) {
        presenterInstancePool.getPresenterInstance(viewObject).apply(viewObject);
    }

    public static void dismiss(Object viewObject){
        presenterInstancePool.getPresenterInstance(viewObject).releaseCurrentView();
    }


}
