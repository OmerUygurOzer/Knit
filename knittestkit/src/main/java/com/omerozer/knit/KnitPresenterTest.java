package com.omerozer.knit;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by omerozer on 2/20/18.
 */

@RunWith(JUnit4.class)
public abstract class KnitPresenterTest<T extends KnitPresenter,K> implements BaseTest {

    private T testSubject;

    @Before
    public void init(){
        setup();
    }

    public void setup(){

    }

    protected abstract K getContract();

    protected abstract KnitNavigator getNavigator();

    protected abstract Class<T> getPresenterClass();

    protected T getPresenter(){
        if(testSubject==null){
            testSubject = KnitTestKit
                    .presenterBuilder(getPresenterClass())
                    .setKnit(getKnitInstance())
                    .setModelManager(getModelManager())
                    .setNavigator(getNavigator())
                    .usingContract(getContract())
                    .build();
        }
        return testSubject;
    }

}
