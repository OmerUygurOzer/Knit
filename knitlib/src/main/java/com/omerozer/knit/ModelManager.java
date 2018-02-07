package com.omerozer.knit;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/4/18.
 */

public class ModelManager implements KnitModel {

    private KnitAsyncTaskHandler knitAsyncTaskHandler;

    private Map<String, KnitModel> valueToModelMap;
    private String[] valuesHandled;

    private final Object requestLock = new Object();

    public ModelManager(Class<?> clazz,KnitClassLoader knitClassLoader,KnitAsyncTaskHandler knitAsyncTaskHandler) {
        this.knitAsyncTaskHandler = knitAsyncTaskHandler;
        ModelMapInterface modelMap = ModelCreator.create(clazz,knitClassLoader);
        this.valueToModelMap = new LinkedHashMap<>();
        for (KnitModel model : modelMap.getAll(knitAsyncTaskHandler)) {
            for (String val : model.getHandledValues()) {
                valueToModelMap.put(val, model);
            }
        }
        this.valuesHandled = new String[valueToModelMap.size()];
        this.valuesHandled = valueToModelMap.keySet().toArray(valuesHandled);
    }

    @Override
    public void request(String[] params, KnitPresenter presenter) {
        synchronized (requestLock) {
            for (String param : params) {
                if(valueToModelMap.containsKey(param)){
                    valueToModelMap.get(param).request(params, presenter);
                }
            }
        }
    }

    @Override
    public String[] getHandledValues() {
        return valuesHandled;
    }
}
