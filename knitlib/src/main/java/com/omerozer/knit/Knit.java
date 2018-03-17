package com.omerozer.knit;

import android.app.Application;

import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.classloaders.KnitPresenterLoader;
import com.omerozer.knit.classloaders.KnitUtilsLoader;
import com.omerozer.knit.components.KnitMemoryManager;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.components.graph.UsageGraph;
import com.omerozer.knit.schedulers.SchedulerProvider;
import com.omerozer.knit.schedulers.Schedulers;

import java.lang.ref.WeakReference;

/**
 * Created by omerozer on 2/1/18.
 */

public class Knit implements KnitInterface {

    private static Knit instance;

    public static void init(Application application) {
        instance = new Knit(application);
    }

    public static Knit getInstance(){
        return instance;
    }

    private KnitModelLoader knitModelLoader;

    private KnitPresenterLoader knitPresenterLoader;

    private KnitUtilsLoader knitUtilsLoader;

    private UsageGraph userGraph;

    private SchedulerProvider schedulerProvider;

    private ModelManager modelManager;

    private KnitNavigator navigator;

    private WeakReference<Application> app;

    private ViewEvents viewEvents;

    private MessagePool messagePool;

    private MessageTrain messageTrain;

    private Knit(Application application){
        this.app = new WeakReference<Application>(application);
        this.modelManager = new ModelManager();
        this.schedulerProvider = new Schedulers();
        this.navigator = new KnitNavigator(this);
        this.knitModelLoader = new KnitModelLoader(schedulerProvider);
        this.knitPresenterLoader = new KnitPresenterLoader(this);
        this.knitUtilsLoader = new KnitUtilsLoader();
        this.userGraph = new UsageGraph(this);
        this.messagePool = new MessagePool();
        application.registerComponentCallbacks(new KnitMemoryManager(userGraph));
        application.registerActivityLifecycleCallbacks(new KnitAppListener(this));
    }

    public Application getApp(){
        return app.get();
    }

    void initViewDependencies(Object viewObject) {
        navigator.navigatedTo(viewObject);
        userGraph.startViewAndItsComponents(viewObject);
    }

    void releaseViewFromComponent(Object viewObject) {
       userGraph.releaseViewFromComponent(viewObject);
    }

    void destoryComponent(Object viewObject){
        userGraph.stopViewAndItsComponents(viewObject);
    }

    public InternalPresenter findPresenterForView(Object viewObject) {
        return userGraph.getPresenterForView(viewObject);
    }

    public InternalPresenter findPresenterForParent(Object parentPresenter) {
        return userGraph.getPresenterForObject(parentPresenter);
    }

    @Override
    public SchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }

    @Override
    public ModelManager getModelManager(){
        return modelManager;
    }

    @Override
    public KnitNavigator getNavigator() {
        return navigator;
    }

    @Override
    public KnitModelLoader getModelLoader() {
        return knitModelLoader;
    }

    @Override
    public KnitPresenterLoader getPresenterLoader() {
        return knitPresenterLoader;
    }

    @Override
    public KnitUtilsLoader getUtilsLoader() {
        return knitUtilsLoader;
    }

    @Override
    public ViewEvents getViewEvents() {
        if(viewEvents == null){
            viewEvents = new ViewEvents(this);
        }
        return viewEvents;
    }

    @Override
    public MessagePool getMessagePool() {
        return messagePool;
    }

    @Override
    public MessageTrain getMessageTrain() {
        if(messageTrain == null){
            messageTrain = new MessageTrain();
        }
        return messageTrain;
    }
}
