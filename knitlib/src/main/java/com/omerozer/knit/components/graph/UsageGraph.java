package com.omerozer.knit.components.graph;

import android.os.Bundle;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.InternalPresenter;
import com.omerozer.knit.Knit;
import com.omerozer.knit.KnitNavigator;
import com.omerozer.knit.MemoryEntity;
import com.omerozer.knit.ModelMapInterface;
import com.omerozer.knit.ViewToPresenterMapInterface;
import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.classloaders.KnitPresenterLoader;
import com.omerozer.knit.classloaders.KnitUtilsLoader;
import com.omerozer.knit.components.ComponentTag;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.schedulers.SchedulerProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by omerozer on 2/16/18.
 */

public class UsageGraph {

    private Knit knitInstance;

    private ViewToPresenterMapInterface viewToPresenterMap;

    private ModelMapInterface modelMap;

    private ModelManager modelManager;

    private KnitUtilsLoader knitUtilsLoader;

    private KnitModelLoader knitModelLoader;

    private KnitPresenterLoader knitPresenterLoader;

    private Map<ComponentTag, UserCounter> counterMap;

    private Map<ComponentTag, EntityNode> graphBase;

    private Map<Class<?>, ComponentTag> clazzToTagMap;

    private Map<ComponentTag, Class<?>> tagToClazzMap;

    private Map<ComponentTag, MemoryEntity> instanceMap;

    private Set<ComponentTag> activeModelTags;

    private Set<ComponentTag> activePresenterTags;

    public UsageGraph(Knit knitInstance, SchedulerProvider schedulerProvider, KnitNavigator navigator,
            ModelManager modelManager) {
        this.knitUtilsLoader = new KnitUtilsLoader();
        this.modelManager = modelManager;
        this.modelManager.setUsageGraph(this);
        this.viewToPresenterMap = knitUtilsLoader.getViewToPresenterMap(knitInstance.getClass());
        this.modelMap = knitUtilsLoader.getModelMap(knitInstance.getClass());
        this.knitModelLoader = new KnitModelLoader(schedulerProvider);
        this.knitPresenterLoader = new KnitPresenterLoader(knitInstance,navigator, modelManager);
        this.counterMap = new HashMap<>();
        this.graphBase = new HashMap<>();
        this.clazzToTagMap = new HashMap<>();
        this.tagToClazzMap = new HashMap<>();
        this.instanceMap = new HashMap<>();
        this.activeModelTags = new HashSet<>();
        this.activePresenterTags = new HashSet<>();
        createGraph();
    }

    private void createGraph() {
        List<Class<? extends InternalModel>> models = modelMap.getAll();
        List<Class<?>> views = extractViews(viewToPresenterMap.getAllViews());
        Map<Class<? extends InternalModel>, List<String>> generatedValuesMap = new HashMap<>();
        for (Class<? extends InternalModel> clazz : models) {
            generatedValuesMap.put(clazz, modelMap.getGeneratedValues(clazz));
        }
        Map<Class<? extends InternalModel>, List<String>> requiredValues = new HashMap<>();
        for (Class<? extends InternalModel> clazz : models) {
            requiredValues.put(clazz, modelMap.getRequiredValues(clazz));
        }

        for (Class<?> view : views) {
            ComponentTag viewTag = ComponentTag.getNewTag();
            counterMap.put(viewTag, new UserCounter());
            graphBase.put(viewTag, new EntityNode(viewTag, EntityType.VIEW));
            clazzToTagMap.put(view, viewTag);
            tagToClazzMap.put(viewTag, view);
            Class<?> presenterClass = viewToPresenterMap.getPresenterClassForView(view);
            ComponentTag presenterTag = ComponentTag.getNewTag();
            counterMap.put(presenterTag, new UserCounter());
            clazzToTagMap.put(presenterClass, presenterTag);
            tagToClazzMap.put(presenterTag, presenterClass);
            EntityNode presenterEntityNode = new EntityNode(presenterTag, EntityType.PRESENTER);
            graphBase.get(viewTag).next.add(presenterEntityNode);
            for (String updating : viewToPresenterMap.getUpdatingValues(presenterClass)) {
                Iterator<Class<? extends InternalModel>> modelOuterItr = models.iterator();
                while (modelOuterItr.hasNext()) {
                    Class<? extends InternalModel> modelClazz = modelOuterItr.next();
                       createModelTag(modelClazz);
                    if (generatedValuesMap.get(modelClazz).contains(updating)) {
                        EntityNode node = new EntityNode(clazzToTagMap.get(modelClazz), EntityType.MODEL);
                        Iterator<Class<? extends InternalModel>> modelInnerItr = models.iterator();
                        while (modelInnerItr.hasNext()) {
                            Class<? extends InternalModel> innerClazz = modelInnerItr.next();
                            for(String req: requiredValues.get(modelClazz)){
                                if(generatedValuesMap.get(innerClazz).contains(req)){
                                    createModelTag(innerClazz);
                                    EntityNode reqM = new EntityNode(clazzToTagMap.get(innerClazz), EntityType.MODEL);
                                    node.next.add(reqM);
                                }
                            }

                        }

                        presenterEntityNode.next.add(node);
                    }

                }
            }
        }

    }

    private void createModelTag(Class<? extends InternalModel> clazz){
        if (!clazzToTagMap.containsKey(clazz)) {
            ComponentTag modelTag = ComponentTag.getNewTag();
            clazzToTagMap.put(clazz, modelTag);
            counterMap.put(modelTag, new UserCounter());
            tagToClazzMap.put(modelTag, clazz);
        }
    }

    public Collection<MemoryEntity> activeEntities(){
        return instanceMap.values();
    }

    public InternalModel getModelWithTag(ComponentTag componentTag){
        return (InternalModel) instanceMap.get(componentTag);
    }

    public InternalPresenter getPresenterTag(ComponentTag componentTag){
        return (InternalPresenter) instanceMap.get(componentTag);
    }

    public InternalPresenter getPresenterForObject(Object presenterObject){
        return (InternalPresenter) instanceMap.get(clazzToTagMap.get(viewToPresenterMap.getPresenterClassForPresenter(presenterObject.getClass())));
    }

    public InternalPresenter getPresenterForView(Object viewObject){
        return (InternalPresenter)instanceMap.get(clazzToTagMap.get(viewToPresenterMap.getPresenterClassForView(viewObject.getClass())));
    }

    void attachViewToComponent(Object viewObject,Bundle bundle){
        for(EntityNode presenter:graphBase.get(clazzToTagMap.get(viewObject.getClass())).next){
            if(instanceMap.containsKey(presenter.tag)){
                ((InternalPresenter) instanceMap.get(presenter.tag)).onViewApplied(viewObject,bundle);
            }
        }
    }

    public void releaseViewFromComponent(Object viewObject){
        for(EntityNode presenter:graphBase.get(clazzToTagMap.get(viewObject.getClass())).next){
            if(instanceMap.containsKey(presenter.tag)){
                ((InternalPresenter) instanceMap.get(presenter.tag)).onCurrentViewReleased();
            }
        }
    }

    private boolean isComponentCreated(Object viewObject){
        for(EntityNode entityNode:graphBase.get(clazzToTagMap.get(viewObject.getClass())).next){
            if(instanceMap.containsKey(entityNode.tag)){
                return true;
            }
        }
        return false;
    }

    public void startViewAndItsComponents(Object viewObject, Bundle data) {
        if(isComponentCreated(viewObject)){
            attachViewToComponent(viewObject,data);
            return;
        }

        Class<?> clazz = viewObject.getClass();
        if(!clazzToTagMap.containsKey(clazz)){
            return;
        }
        recurseTraverseTheGraphAndStartIfNeeded(clazzToTagMap.get(clazz), viewObject, data);
    }

    private void recurseTraverseTheGraphAndStartIfNeeded(ComponentTag tag, Object viewObject,
            Bundle data) {
        recurseForStart(graphBase.get(tag), viewObject, data);
    }

    private void recurseForStart(EntityNode entityNode, Object viewObject, Bundle data) {
        for (EntityNode node : entityNode.next) {
            recurseForStart(node, viewObject, data);
        }
        switch (entityNode.type) {
            case MODEL:
                if (!counterMap.get(entityNode.tag).isUsed()) {
                    InternalModel internalModel = knitModelLoader.loadModel(
                            tagToClazzMap.get(entityNode.tag));
                    instanceMap.put(entityNode.tag, internalModel);
                    activeModelTags.add(entityNode.tag);
                    modelManager.registerModelComponentTag(entityNode.tag);
                    internalModel.onCreate();
                }
                break;

            case PRESENTER:
                if (!counterMap.get(entityNode.tag).isUsed()) {
                    InternalPresenter internalPresenter = knitPresenterLoader.loadPresenter(tagToClazzMap.get(entityNode.tag));
                    instanceMap.put(entityNode.tag, internalPresenter);
                    activePresenterTags.add(entityNode.tag);
                    internalPresenter.onCreate();
                }
                ((InternalPresenter) instanceMap.get(entityNode.tag)).onViewApplied(viewObject, data);
                break;
        }
        counterMap.get(entityNode.tag).use();

    }

    public void stopViewAndItsComponents(Object viewObject) {
        recurseTraverseTheGraphAndDestroyIfNeeded(clazzToTagMap.get(viewObject.getClass()));
    }

    private void recurseTraverseTheGraphAndDestroyIfNeeded(ComponentTag tag) {
        recurseForFinish(graphBase.get(tag));
    }

    private void recurseForFinish(EntityNode entityNode) {
        counterMap.get(entityNode.tag).release();
        switch (entityNode.type) {
            case MODEL:
                if(!counterMap.get(entityNode.tag).isUsed()){
                    instanceMap.get(entityNode.tag).onDestroy();
                    instanceMap.remove(entityNode.tag);
                    activeModelTags.remove(entityNode.tag);
                    modelManager.unregisterComponentTag(entityNode.tag);
                }

                break;
            case PRESENTER:
                if (!counterMap.get(entityNode.tag).isUsed()) {
                    instanceMap.get(entityNode.tag).onDestroy();
                    instanceMap.remove(entityNode.tag);
                    activePresenterTags.remove(entityNode.tag);
                }
                break;
        }

        for (EntityNode node : entityNode.next) {
            recurseForFinish(node);
        }
    }

    private List<Class<?>> extractViews(List<Class<?>> views) {
        return views.subList(0, views.size() - 1);
    }


}
