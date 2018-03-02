package com.omerozer.knit;

import com.omerozer.knit.schedulers.SchedulerProvider;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by omerozer on 2/20/18.
 */

@RunWith(JUnit4.class)
public abstract class KnitModelTest<T extends KnitModel>{

    private T testSubject;

    private TestModelManager modelManager;

    @BeforeClass
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
        if(testSubject==null){
             testSubject = (T)modelManager.registerModel(getModelClass());
        }
        return testSubject;
    }

}
