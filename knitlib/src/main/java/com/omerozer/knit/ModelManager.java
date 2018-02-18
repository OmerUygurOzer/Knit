package com.omerozer.knit;

import com.omerozer.knit.classloaders.KnitUtilsLoader;
import com.omerozer.knit.generators.Callback;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/4/18.
 */

public class ModelManager extends InternalModel {

    private KnitAsyncTaskHandler knitAsyncTaskHandler;

    private Map<String, InternalModel> valueToModelMap;
    private String[] valuesHandled;

    private final Object requestLock = new Object();

    public ModelManager(Class<?> clazz, KnitUtilsLoader knitUtilsLoader,
            KnitAsyncTaskHandler knitAsyncTaskHandler) {
        this.knitAsyncTaskHandler = knitAsyncTaskHandler;
        ModelMapInterface modelMap = ModelCreator.create(clazz, knitUtilsLoader);
        this.valueToModelMap = new LinkedHashMap<>();
//        for (InternalModel model : modelMap.getAll(knitAsyncTaskHandler)) {
//            for (String val : model.getHandledValues()) {
//                valueToModelMap.put(val, model);
//                model.getParent().setModelManager(this);
//                model.onCreate();
//            }
//        }
        this.valuesHandled = new String[valueToModelMap.size()];
        this.valuesHandled = valueToModelMap.keySet().toArray(valuesHandled);
    }

    @Override
    public void request(String data, InternalPresenter internalPresenter, Object... params) {
        synchronized (requestLock) {
            if (valueToModelMap.containsKey(data)) {
                valueToModelMap.get(data).request(data, internalPresenter, params);
            }
        }
    }

    @Override
    public void request(String data, Callback callback, Object... params) {
        synchronized (requestLock) {
            if (valueToModelMap.containsKey(data)) {
                valueToModelMap.get(data).request(data, callback, params);
            }
        }
    }

    @Override
    public void input(String data, Object... params) {
        synchronized (requestLock) {
            if (valueToModelMap.containsKey(data)) {
                valueToModelMap.get(data).input(data, params);
            }
        }
    }

    @Override
    public KnitModel getParent() {
        return null;
    }

    @Override
    public String[] getHandledValues() {
        return valuesHandled;
    }
}
