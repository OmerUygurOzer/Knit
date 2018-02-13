package com.omerozer.knit;

/**
 * Created by omerozer on 2/4/18.
 */

class ModelCreator {
    static ModelMapInterface create(Class<?> clazz,KnitUtilsLoader knitUtilsLoader){
        return knitUtilsLoader.getModelMap(clazz);
    }
}
