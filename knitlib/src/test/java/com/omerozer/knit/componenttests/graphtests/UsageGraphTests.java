package com.omerozer.knit.componenttests.graphtests;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.InternalPresenter;
import com.omerozer.knit.KnitInterface;
import com.omerozer.knit.KnitMock;
import com.omerozer.knit.ModelMap;
import com.omerozer.knit.TestEnv;
import com.omerozer.knit.TestModel2_Model;
import com.omerozer.knit.TestModel_Model;
import com.omerozer.knit.TestPresenter_Presenter;
import com.omerozer.knit.TestSingleton_Model;
import com.omerozer.knit.UmbrellaModel_Model;
import com.omerozer.knit.View1;
import com.omerozer.knit.ViewToPresenterMap;
import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.classloaders.KnitPresenterLoader;
import com.omerozer.knit.components.ComponentTag;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.components.graph.UsageGraph;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Omer Ozer on 3/12/2018.
 */

public class UsageGraphTests {

    KnitInterface knit;

    UsageGraph usageGraph;

    ModelMap modelMap;

    ViewToPresenterMap viewToPresenterMap;

    ModelManager modelManager;

    KnitModelLoader modelLoader;

    KnitPresenterLoader presenterLoader;

    @Captor
    ArgumentCaptor<Class<? extends InternalModel>> internalModelCaptor;

    @Captor
    ArgumentCaptor<Class<? extends InternalPresenter>> internalPresenterCaptor;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        ComponentTag.reset();
        this.knit = KnitMock.get();
        this.modelManager = knit.getModelManager();
        this.modelLoader = knit.getModelLoader();
        this.presenterLoader = knit.getPresenterLoader();
        this.modelMap = ModelMap.getMock();
        this.viewToPresenterMap = ViewToPresenterMap.getMock();
        this.usageGraph = new UsageGraph(knit);
    }

    @Test
    public void graphInitializationTest(){
        Short cur = ComponentTag.getNewTag().getTag();
        assertEquals(Short.valueOf("-32762"),cur);
        verify(modelMap).getAll();
        verify(viewToPresenterMap).getAllViews();
        verify(modelMap,times(TestEnv.totalModels())).getGeneratedValues(any(Class.class));
        verify(modelMap,times(TestEnv.totalModels())).getRequiredValues(any(Class.class));
        verify(viewToPresenterMap,times(TestEnv.totalPresentersWithDependencies())).getUpdatingValues(any(Class.class));
    }

    @Test
    public void startViewComponentTest(){
        usageGraph.startViewAndItsComponents(new View1(),null);
        verify(modelManager,times(TestEnv.totalModels())).registerModelComponentTag(any(ComponentTag.class));
        verify(modelLoader,times(TestEnv.totalModels())).loadModel(internalModelCaptor.capture());
        assertEquals(TestEnv.totalModels()+TestEnv.totalPresentersWithDependencies(),usageGraph.activeEntities().size());
        assertTrue(internalModelCaptor.getAllValues().contains(TestModel_Model.class));
        assertTrue(internalModelCaptor.getAllValues().contains(TestModel2_Model.class));
        assertTrue(internalModelCaptor.getAllValues().contains(UmbrellaModel_Model.class));
        assertTrue(internalModelCaptor.getAllValues().contains(TestSingleton_Model.class));
        verify(presenterLoader).loadPresenter(internalPresenterCaptor.capture());
        assertEquals(TestPresenter_Presenter.class,internalPresenterCaptor.getValue());
    }

    @Test
    public void destroyViewComponentsTest(){
        View1 view1 = new View1();
        usageGraph.startViewAndItsComponents(view1,null);
        usageGraph.stopViewAndItsComponents(view1);
        verify(modelManager,times(TestEnv.nonSingleTonModels())).unregisterComponentTag(any(ComponentTag.class));
    }

}
