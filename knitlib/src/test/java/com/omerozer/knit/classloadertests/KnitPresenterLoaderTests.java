package com.omerozer.knit.classloadertests;

import static junit.framework.Assert.assertEquals;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.InternalPresenter;
import com.omerozer.knit.Knit;
import com.omerozer.knit.KnitInterface;
import com.omerozer.knit.KnitMock;
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

    KnitInterface knit;

    KnitNavigator knitNavigator;

    InternalModel modelManager;

    KnitPresenterLoader knitPresenterLoader;

    @Before
    public void setup(){
        this.knit = KnitMock.get();
        this.knitNavigator = knit.getNavigator();
        this.modelManager = knit.getModelManager();
        this.knitPresenterLoader = new KnitPresenterLoader(knit);
    }

    @Test
    public void loadPresenterTest(){
        InternalPresenter internalPresenter = knitPresenterLoader.loadPresenter(TestPresenter_Presenter.class);
        assertEquals(TestPresenter_Presenter.class,internalPresenter.getClass());
        TestPresenter_Presenter castPresenter = (TestPresenter_Presenter)internalPresenter;
        assertEquals(knit,castPresenter.getKnit());
        assertEquals(knitNavigator,castPresenter.getNavigator());
        assertEquals(modelManager,castPresenter.getModelManager());
    }



}
