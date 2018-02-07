package com.omerozer.knit;

/**
 * Created by omerozer on 2/4/18.
 */

class ModelCreator {
    static ModelMapInterface create(Class<?> clazz,KnitClassLoader knitClassLoader){
        return knitClassLoader.getModelMap(clazz);
    }
}
