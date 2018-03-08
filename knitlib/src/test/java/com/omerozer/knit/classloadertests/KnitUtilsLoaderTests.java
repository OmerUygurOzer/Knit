package com.omerozer.knit.classloadertests;

import static junit.framework.Assert.assertEquals;

import com.omerozer.knit.ModelMap;
import com.omerozer.knit.ModelMapInterface;
import com.omerozer.knit.ViewToPresenterMap;
import com.omerozer.knit.ViewToPresenterMapInterface;
import com.omerozer.knit.classloaders.KnitUtilsLoader;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by omerozer on 3/8/18.
 */

public class KnitUtilsLoaderTests {

    KnitUtilsLoader knitUtilsLoader;

    @Before
    public void setup(){
        this.knitUtilsLoader = new KnitUtilsLoader();
    }

    @Test
    public void createModelMapTest(){
        ModelMapInterface modelMapInterface = knitUtilsLoader.getModelMap(getClass());
        assertEquals(ModelMap.class,modelMapInterface.getClass());
    }

    @Test
    public void createViewToPresenterMapTest(){
        ViewToPresenterMapInterface viewToPresenterMap = knitUtilsLoader.getViewToPresenterMap(getClass());
        assertEquals(ViewToPresenterMap.class,viewToPresenterMap.getClass());
    }


}
