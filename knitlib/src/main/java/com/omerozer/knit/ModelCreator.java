package com.omerozer.knit;

import com.omerozer.knit.classloaders.KnitUtilsLoader;

/**
 * Created by omerozer on 2/4/18.
 */

class ModelCreator {
    static ModelMapInterface create(Class<?> clazz,KnitUtilsLoader knitUtilsLoader){
        return knitUtilsLoader.getModelMap(clazz);
    }
}
