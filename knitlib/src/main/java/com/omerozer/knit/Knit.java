package com.omerozer.knit;

import android.app.Application;

import com.omerozer.knit.components.KnitMemoryManager;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.components.graph.UsageGraph;
import com.omerozer.knit.schedulers.SchedulerProvider;
import com.omerozer.knit.schedulers.Schedulers;

import java.lang.ref.WeakReference;

/**
 * Created by omerozer on 2/1/18.
 */

public final class Knit {

    private static Knit instance;

    public static void init(Application application) {
        instance = new Knit(application);
    }

    public static Knit getInstance(){
        return instance;
    }

    private UsageGraph userGraph;

    private SchedulerProvider schedulerProvider;

    private ModelManager modelManager;

    private KnitNavigator navigator;

    private WeakReference<Application> app;

    private Knit(Application application){
        this.app = new WeakReference<Application>(application);
        this.modelManager = new ModelManager();
        this.schedulerProvider = new Schedulers();
        this.navigator = KnitNavigator.getInstance();
        this.userGraph = new UsageGraph(this,schedulerProvider,navigator,modelManager);
        application.registerComponentCallbacks(new KnitMemoryManager(userGraph));
        application.registerActivityLifecycleCallbacks(new KnitAppListener(this));
        KnitEvents.init(this);
    }

    public Application getApp(){
        return app.get();
    }

    void initViewDependencies(Object viewObject) {
        navigator.navigatedTo(viewObject);
        userGraph.startViewAndItsComponents(viewObject,navigator.getDataForTarget(viewObject));
    }

    void releaseViewFromComponent(Object viewObject) {
       userGraph.releaseViewFromComponent(viewObject);
    }

    void destoryComponent(Object viewObject){
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
