package com.omerozer.knit.components;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.InternalPresenter;
import com.omerozer.knit.KnitModel;
import com.omerozer.knit.KnitResponse;
import com.omerozer.knit.components.graph.UsageGraph;
import com.omerozer.knit.schedulers.KnitSchedulers;

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
        if(usageGraph.getModelWithTag(componentTag)==null){
            return;
        }
        for (String val : usageGraph.getModelWithTag(componentTag).getHandledValues()) {
            valueToModelMap.remove(val);
        }
    }

    @Override
    public void request(String data,KnitSchedulers runOn,KnitSchedulers consumeOn,InternalPresenter internalPresenter, Object... params) {
        synchronized (requestLock) {
            if (valueToModelMap.containsKey(data)) {
                usageGraph.getModelWithTag(valueToModelMap.get(data)).request(data, runOn,consumeOn,internalPresenter, params);
            }
        }
    }

    @Override
    public <T> KnitResponse<T> requestImmediately(String data, Object... params) {
        synchronized (requestLock) {
            if (valueToModelMap.containsKey(data)) {
                return usageGraph.getModelWithTag(valueToModelMap.get(data)).requestImmediately(data, params);
            }
        }
        return null;
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
