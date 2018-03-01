package com.omerozer.knit;

import com.omerozer.knit.classloaders.KnitModelLoader;
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

    public TestModelManager(SchedulerProvider schedulerProvider){
        this.dataToModelMap = new HashMap<>();
        this.modelLoader = new KnitModelLoader(schedulerProvider==null? new TestSchedulers() : schedulerProvider);
    }

    KnitModel registerModel(Class<? extends KnitModel> model){
        InternalModel internalModel = modelLoader.loadModel(modelLoader.getModelForModel(model));
        for(String val: internalModel.getHandledValues()){
            dataToModelMap.put(val,internalModel);
        }
        return internalModel.getParent();
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
