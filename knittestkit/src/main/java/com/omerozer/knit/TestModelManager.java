package com.omerozer.knit;

import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.classloaders.KnitUtilsLoader;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.schedulers.KnitSchedulers;
import com.omerozer.knit.schedulers.SchedulerProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/20/18.
 */

public final class TestModelManager extends ModelManager {

    private Map<String,InternalModel> dataToModelMap = new HashMap<>();
    private KnitModelLoader modelLoader;
    private KnitUtilsLoader utilsLoader;
    private ModelMapInterface modelMapInterface;

    public TestModelManager(SchedulerProvider schedulerProvider){
        this.dataToModelMap = new HashMap<>();
        this.modelLoader = new KnitModelLoader(schedulerProvider==null? new TestSchedulers() : schedulerProvider);
        this.utilsLoader = new KnitUtilsLoader();
        this.modelMapInterface = utilsLoader.getModelMap(Knit.class);
    }

    KnitModel registerModel(Class<? extends KnitModel> model){
        InternalModel internalModel = modelLoader.loadModel(modelLoader.getModelForModel(model));
        registerInternalModel(internalModel);
        return internalModel.getParent();
    }


    InternalModel registerModelMock(Class<? extends KnitModel> modelClazz,Mocker mocker){
        Class<? extends InternalModel> internalClazz = modelMapInterface.getModelClassForModel(modelClazz);
        InternalModel internalModel = mocker.mock(internalClazz);
        registerInternalModel(internalModel);
        return internalModel;
    }

    private void registerInternalModel(InternalModel internalModel){
        for(String val: internalModel.getHandledValues()){
            dataToModelMap.put(val,internalModel);
        }
    }


    @Override
    public void input(String data, Object... params) {
        if(dataToModelMap.containsKey(data)){
            dataToModelMap.get(data).input(data,params);
        }
    }

    @Override
    public void request(String data, KnitSchedulers runOn, KnitSchedulers consumeOn,
            InternalPresenter internalPresenter, Object... params) {
        if(dataToModelMap.containsKey(data)){
            dataToModelMap.get(data).request(data,runOn,consumeOn,internalPresenter,params);
        }
    }

    @Override
    public <T> KnitResponse<T> requestImmediately(String data, Object... params) {
        if(dataToModelMap.containsKey(data)){
            return dataToModelMap.get(data).requestImmediately(data,params);
        }
        return null;
    }
}
