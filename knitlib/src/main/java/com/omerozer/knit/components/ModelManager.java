package com.omerozer.knit.components;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.InternalPresenter;
import com.omerozer.knit.KnitAsyncTaskHandler;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.classloaders.KnitUtilsLoader;
import com.omerozer.knit.components.graph.UsageGraph;
import com.omerozer.knit.generators.Callback;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/4/18.
 */

public class ModelManager extends InternalModel {

    private UsageGraph usageGraph;

    private Map<String, ComponentTag> valueToModelMap;

    private final Object requestLock = new Object();

    public ModelManager() {
        this.valueToModelMap = new LinkedHashMap<>();
    }

    public void setUsageGraph(UsageGraph usageGraph) {
        this.usageGraph = usageGraph;
    }

    public void registerModelComponentTag(ComponentTag componentTag) {
        for (String val : usageGraph.getModelWithTag(componentTag).getHandledValues()) {
            valueToModelMap.put(val, componentTag);
            usageGraph.getModelWithTag(componentTag).getParent().setModelManager(this);
        }
    }

    public void unregisterComponentTag(ComponentTag componentTag) {
        for (String val : usageGraph.getModelWithTag(componentTag).getHandledValues()) {
            valueToModelMap.remove(val);
        }
    }

    @Override
    public void request(String data, InternalPresenter internalPresenter, Object... params) {
        synchronized (requestLock) {
            if (valueToModelMap.containsKey(data)) {
                usageGraph.getModelWithTag(valueToModelMap.get(data)).request(data, internalPresenter, params);
            }
        }
    }

    @Override
    public void request(String data, Callback callback, Object... params) {
        synchronized (requestLock) {
            if (valueToModelMap.containsKey(data)) {
                usageGraph.getModelWithTag(valueToModelMap.get(data)).request(data, callback, params);
            }
        }
    }

    @Override
    public void input(String data, Object... params) {
        synchronized (requestLock) {
            if (valueToModelMap.containsKey(data)) {
                usageGraph.getModelWithTag(valueToModelMap.get(data)).input(data, params);
            }
        }
    }

    @Override
    public KnitModel getParent() {
        return null;
    }

    @Override
    public String[] getHandledValues() {
        return new String[0];
    }
}
