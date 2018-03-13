package com.omerozer.knit;

import java.util.List;

/**
 * Created by omerozer on 3/8/18.
 */

public class ModelMap implements ModelMapInterface {
    @Override
    public List<Class<? extends InternalModel>> getAll() {
        return null;
    }

    @Override
    public List<String> getGeneratedValues(Class<?> clazz) {
        return null;
    }

    @Override
    public List<String> getRequiredValues(Class<?> clazz) {
        return null;
    }

    @Override
    public boolean isModelSingleton(Class<? extends InternalModel> clazz) {
        return false;
    }

    @Override
    public Class<? extends InternalModel> getModelClassForModel(Class<? extends KnitModel> target) {
        return TestModel_Model.class;
    }
}
