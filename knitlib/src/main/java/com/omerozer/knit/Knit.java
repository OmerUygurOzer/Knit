package com.omerozer.knit;

import android.app.Application;

import com.omerozer.knit.components.KnitMemoryManager;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.components.graph.UsageGraph;

/**
 * Created by omerozer on 2/1/18.
 */

public class Knit {

    private static Knit instance;

    public static void init(Application application) {
        instance = new Knit(application);
    }

    static Knit getInstance(){
        return instance;
    }

    private UsageGraph userGraph;

    private KnitAsyncTaskHandler knitAsyncTaskHandler;

    private ModelManager modelManager;

    private KnitNavigator navigator;

    private Knit(Application application){
        modelManager = new ModelManager();
        knitAsyncTaskHandler = new KnitAsyncTaskHandler();
        navigator = KnitNavigator.getInstance();
        userGraph = new UsageGraph(this,knitAsyncTaskHandler,navigator,modelManager);
        application.registerComponentCallbacks(new KnitMemoryManager(userGraph));
        application.registerActivityLifecycleCallbacks(new KnitAppListener(this));
        KnitEvents.init(this);
    }

    void initViewDependencies(Object viewObject) {
        navigator.navigatedTo(viewObject);
        userGraph.startViewAndItsComponents(viewObject,navigator.getDataForTarget(viewObject));
    }

    void destroyViewDependencies(Object viewObject) {
       userGraph.stopViewAndItsComponents(viewObject);
    }

    InternalPresenter findPresenterForView(Object viewObject) {
        return userGraph.getPresenterForView(viewObject);
    }

    InternalPresenter findPresenterForParent(Object parentPresenter) {
        return userGraph.getPresenterForObject(parentPresenter);
    }

    InternalModel getModelManager(){
        return modelManager;
    }


}
