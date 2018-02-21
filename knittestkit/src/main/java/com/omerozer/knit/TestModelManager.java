package com.omerozer.knit;

import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.generators.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/20/18.
 */

public final class TestModelManager extends ModelManager {

    private Map<String,InternalModel> dataToModelMap = new HashMap<>();
    private KnitModelLoader modelLoader;

    public TestModelManager(){
        this.dataToModelMap = new HashMap<>();
        this.modelLoader = new KnitModelLoader(new KnitAsyncTaskHandler());
    }

    KnitModel registerModel(Class<? extends KnitModel> model){
        InternalModel internalModel = modelLoader.loadModel(modelLoader.getModelForModel(model));
        for(String val: internalModel.getHandledValues()){
            dataToModelMap.put(val,internalModel);
        }
        return internalModel.getParent();
    }

    protected <T> void requestThreadSafe(String data, final ImmutableHolder<T> holder, Object... params){
        request(data, new Callback<T>() {
            @Override
            public void response(KnitResponse<T> response) {
                holder.setData(response.getBody());
            }
        },params);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void request(String data, Callback callback, Object... params) {
        if(dataToModelMap.containsKey(data)){
            dataToModelMap.get(data).request(data,callback,params);
        }
    }

    @Override
    public void input(String data, Object... params) {
        if(dataToModelMap.containsKey(data)){
            dataToModelMap.get(data).input(data,params);
        }
    }
}
