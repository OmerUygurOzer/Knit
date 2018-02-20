package com.omerozer.knit;

import java.util.List;

/**
 * Created by omerozer on 2/4/18.
 */

public interface ModelMapInterface {
     List<Class<? extends InternalModel>> getAll();
     List<String> getGeneratedValues(Class<?> getGeneratedValues);
     Class<? extends InternalModel> getModelClassForModel(Class<? extends KnitModel> target);
}
