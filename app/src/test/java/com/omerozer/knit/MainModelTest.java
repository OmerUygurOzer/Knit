package com.omerozer.knit;

import static junit.framework.Assert.assertEquals;


import com.omerozer.knit.schedulers.SchedulerProvider;
import com.omerozer.sample.models.MainModel;

import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * Created by omerozer on 2/20/18.
 */

public class MainModelTest extends KnitModelTest<MainModel> {

    MainModel mainModel;

    @Override
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mainModel = getModel();
    }

    @Override
    protected SchedulerProvider schedulerProvider() {
        return null;
    }

    @Test
    public void testGenerateTestString() throws Exception{
        mainModel.generateTestString.generate();
    }

    @Override
    protected Class<MainModel> getModelClass() {
        return MainModel.class;
    }
}
