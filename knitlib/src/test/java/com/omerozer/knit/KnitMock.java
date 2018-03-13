package com.omerozer.knit;

import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.classloaders.KnitPresenterLoader;
import com.omerozer.knit.classloaders.KnitUtilsLoader;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.schedulers.SchedulerProvider;

import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by bo_om on 3/12/2018.
 */

public class KnitMock {

    public static KnitInterface get(){
        Knit knit = Mockito.mock(Knit.class);

        KnitUtilsLoader utilsLoader = Mockito.mock(KnitUtilsLoader.class);
        KnitModelLoader knitModelLoader = Mockito.mock(KnitModelLoader.class);
        KnitPresenterLoader knitPresenterLoader = Mockito.mock(KnitPresenterLoader.class);

        when(knitModelLoader.loadModel(TestModel_Model.class)).thenReturn(new TestModel_Model(new TestSchedulers()));
        when(knitModelLoader.loadModel(TestModel2_Model.class)).thenReturn(new TestModel2_Model());
        when(knitModelLoader.loadModel(TestSingleton_Model.class)).thenReturn(new TestSingleton_Model());
        when(knitModelLoader.loadModel(UmbrellaModel_Model.class)).thenReturn(new UmbrellaModel_Model());

        when(knitPresenterLoader.loadPresenter(TestPresenter_Presenter.class)).thenReturn(new TestPresenter_Presenter());
        when(knitPresenterLoader.loadPresenter(TestPresenter2_Presenter.class)).thenReturn(new TestPresenter2_Presenter());

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
