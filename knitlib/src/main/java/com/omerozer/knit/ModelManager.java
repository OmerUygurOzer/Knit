package com.omerozer.knit;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/4/18.
 */

public class ModelManager implements InternalModel {

    private KnitAsyncTaskHandler knitAsyncTaskHandler;

    private Map<String, InternalModel> valueToModelMap;
    private String[] valuesHandled;

    private final Object requestLock = new Object();

    public ModelManager(Class<?> clazz, KnitUtilsLoader knitUtilsLoader,
            KnitAsyncTaskHandler knitAsyncTaskHandler) {
        this.knitAsyncTaskHandler = knitAsyncTaskHandler;
        ModelMapInterface modelMap = ModelCreator.create(clazz, knitUtilsLoader);
        this.valueToModelMap = new LinkedHashMap<>();
        for (InternalModel model : modelMap.getAll(knitAsyncTaskHandler)) {
            for (String val : model.getHandledValues()) {
                valueToModelMap.put(val, model);
            }
        }
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
    public String[] getHandledValues() {
        return valuesHandled;
    }
}
