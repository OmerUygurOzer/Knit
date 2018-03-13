package com.omerozer.knit;

import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.classloaders.KnitPresenterLoader;
import com.omerozer.knit.classloaders.KnitUtilsLoader;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.schedulers.SchedulerProvider;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Omer Ozer on 3/12/2018.
 */

public class KnitMock {

    public static KnitInterface get(){
        Knit knit = Mockito.mock(Knit.class);

        KnitUtilsLoader utilsLoader = Mockito.mock(KnitUtilsLoader.class);
        KnitModelLoader knitModelLoader = Mockito.mock(KnitModelLoader.class);
        KnitPresenterLoader knitPresenterLoader = Mockito.mock(KnitPresenterLoader.class);

        TestModel_Model mockTestModel = Mockito.mock(TestModel_Model.class);
        TestModel2_Model mockTest2Model = Mockito.mock(TestModel2_Model.class);
        TestSingleton_Model mockSingletonModel = Mockito.mock(TestSingleton_Model.class);
        UmbrellaModel_Model mockUmbrellaModel = Mockito.mock(UmbrellaModel_Model.class);

        when(knitModelLoader.loadModel(TestModel_Model.class)).thenReturn(mockTestModel);
        when(knitModelLoader.loadModel(TestModel2_Model.class)).thenReturn(mockTest2Model);
        when(knitModelLoader.loadModel(TestSingleton_Model.class)).thenReturn(mockSingletonModel);
        when(knitModelLoader.loadModel(UmbrellaModel_Model.class)).thenReturn(mockUmbrellaModel);

        TestPresenter_Presenter mockPresenter = Mockito.mock(TestPresenter_Presenter.class);
        TestPresenter2_Presenter mockPresenter2 = Mockito.mock(TestPresenter2_Presenter.class);

        when(knitPresenterLoader.loadPresenter(TestPresenter_Presenter.class)).thenReturn(mockPresenter);
        when(knitPresenterLoader.loadPresenter(TestPresenter2_Presenter.class)).thenReturn(mockPresenter2);

        ViewToPresenterMap mockViewToPresenterMap = ViewToPresenterMap.getMock();
        when(utilsLoader.getViewToPresenterMap(any(Class.class))).thenReturn(mockViewToPresenterMap);

        ModelMap mockModelMap = ModelMap.getMock();
        when(utilsLoader.getModelMap(any(Class.class))).thenReturn(mockModelMap);

        when(knit.getModelManager()).thenReturn(Mockito.mock(ModelManager.class));
        when(knit.getNavigator()).thenReturn(Mockito.mock(KnitNavigator.class));
        when(knit.getSchedulerProvider()).thenReturn(Mockito.mock(SchedulerProvider.class));
        when(knit.getUtilsLoader()).thenReturn(utilsLoader);
        when(knit.getModelLoader()).thenReturn(knitModelLoader);
        when(knit.getPresenterLoader()).thenReturn(knitPresenterLoader);
        return knit;
    }

}
