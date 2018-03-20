package com.omerozer.knit;

import com.omerozer.knit.schedulers.SchedulerProvider;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.List;

/**
 * Created by omerozer on 2/20/18.
 */

@RunWith(JUnit4.class)
public abstract class KnitModelTest<T extends KnitModel>{

    private TestModelManager modelManager;

    private Mocker mocker;

    @Before
    public void init(){
        modelManager = new TestModelManager(getSchedulerProvider());
        try{
        setup();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setup() throws Exception{

    }


    protected abstract SchedulerProvider getSchedulerProvider();

    protected TestModelManager accessModelManager(){
        return modelManager;
    }

    protected abstract Class<T> getModelClass();

    protected T getModel(){
        return  (T)modelManager.registerModel(getModelClass());
    }

    protected void setMocker(Mocker mocker){
        this.mocker = mocker;
    }

    protected InternalModel addMockModel(Class<? extends KnitModel> modelClazz){
        return modelManager.registerModelMock(modelClazz,mocker);
    }


}
