package com.omerozer.knit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.omerozer.sample.presenters.MainPresenter;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by omerozer on 2/18/18.
 */

public class MainPresenterTest {

    public MainPresenter mainPresenter;

    public InternalModel modelManager;

    public KnitNavigator navigator;

    public Object contract;

    @Before
    public void setup(){
        mainPresenter = KnitTestKit
                .presenterBuilder(MainPresenter.class)
                .setModelManager(modelManager)
                .setNavigator(navigator)
                .usingContract(contract)
                .build();

    }

    @Test
    public void onCreateTest() throws Exception {
        assertNotNull(mainPresenter);
    }

}
