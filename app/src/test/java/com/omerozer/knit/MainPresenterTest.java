package com.omerozer.knit;

import static com.omerozer.knit.NullValues.NULL_PRESENTER;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.omerozer.sample.presenters.MainPresenter;
import com.omerozer.sample.views.MainActivityContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by omerozer on 2/18/18.
 */

public class MainPresenterTest {

    MainPresenter mainPresenter;

    @Mock
    Knit knit;

    @Mock
    InternalModel modelManager;

    @Mock
    KnitNavigator navigator;

    @Mock
    MainActivityContract contract;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        mainPresenter = KnitTestKit
                .presenterBuilder(MainPresenter.class)
                .setKnit(knit)
                .setModelManager(modelManager)
                .setNavigator(navigator)
                .usingContract(contract)
                .build();

        when(knit.findPresenterForParent(mainPresenter)).thenReturn(NULL_PRESENTER);
    }

    @Test
    public void onViewAppliedTest() throws Exception {
        ArgumentCaptor<String> dataRequestCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<InternalPresenter> presenterCaptor = ArgumentCaptor.forClass(InternalPresenter.class);
        ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);
        mainPresenter.onViewApplied(null,null);
        verify(modelManager).request(dataRequestCaptor.capture(),presenterCaptor.capture(),paramsCaptor.capture());
        assertEquals(NULL_PRESENTER,presenterCaptor.getValue());
        assertEquals("test",dataRequestCaptor.getValue());
    }



}
