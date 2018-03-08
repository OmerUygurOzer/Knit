package com.omerozer.knit.classloadertests;

import static junit.framework.Assert.assertEquals;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.InternalPresenter;
import com.omerozer.knit.Knit;
import com.omerozer.knit.KnitNavigator;
import com.omerozer.knit.TestPresenter_Presenter;
import com.omerozer.knit.classloaders.KnitPresenterLoader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by omerozer on 3/8/18.
 */

public class KnitPresenterLoaderTests {

    @Mock
    Knit knit;

    @Mock
    KnitNavigator knitNavigator;

    @Mock
    InternalModel modelManager;

    KnitPresenterLoader knitPresenterLoader;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.knitPresenterLoader = new KnitPresenterLoader(knit,knitNavigator,modelManager);
    }

    @Test
    public void loadPresenterTest(){
        InternalPresenter internalPresenter = knitPresenterLoader.loadPresenter(
                TestPresenter_Presenter.class);
        assertEquals(TestPresenter_Presenter.class,internalPresenter.getClass());
        TestPresenter_Presenter castPresenter = (TestPresenter_Presenter)internalPresenter;
        assertEquals(knit,castPresenter.getKnit());
        assertEquals(knitNavigator,castPresenter.getNavigator());
        assertEquals(modelManager,castPresenter.getModelManager());
    }



}
