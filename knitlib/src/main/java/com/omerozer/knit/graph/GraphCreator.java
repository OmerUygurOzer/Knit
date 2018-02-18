package com.omerozer.knit.graph;

import com.omerozer.knit.ModelMapInterface;
import com.omerozer.knit.ViewToPresenterMapInterface;
import com.omerozer.knit.classloaders.KnitUtilsLoader;

import java.util.List;
import java.util.Map;

/**
 * Created by omerozer on 2/16/18.
 */

public class GraphCreator {

    private Short tag;

    private ViewToPresenterMapInterface viewToPresenterMap;

    private ModelMapInterface modelMap;

    private KnitUtilsLoader knitUtilsLoader;

   // private Map<Short,>

    public GraphCreator(Class<?> base){
        knitUtilsLoader = new KnitUtilsLoader();
        viewToPresenterMap = knitUtilsLoader.getViewToPresenterMap(base);
        modelMap = knitUtilsLoader.getModelMap(base);
        tag = Short.MIN_VALUE;

    }

    private Short getNewTag(){
        return tag++;
    }

    private List<Class<?>> extractViews(List<Class<?>> views){
        return views.subList(0,views.size()-1);
    }


}
