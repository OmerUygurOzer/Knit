package com.omerozer.knit;

import static com.omerozer.knit.NullValues.NULL_PRESENTER;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;

import com.omerozer.sample.presenters.MainPresenter;
import com.omerozer.sample.views.MainActivityContract;
import com.omerozer.sample.views.SecondActivity;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by omerozer on 2/18/18.
 */

public class MainPresenterTest extends KnitPresenterTest<MainPresenter,MainActivityContract> {

    MainPresenter mainPresenter;

    @Mock
    Knit knit;

    @Mock
    InternalModel modelManager;

    @Mock
    KnitNavigator navigator;

    @Mock
    MainActivityContract contract;

    @Mock
    KnitNavigator.ActivityNavigator activityNavigator;

    @Override
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mainPresenter = getPresenter();
        when(knit.findPresenterForParent(mainPresenter)).thenReturn(NULL_PRESENTER);
        when(activityNavigator.target(SecondActivity.class)).thenReturn(activityNavigator);
        when(navigator.toActivity()).thenReturn(activityNavigator);
    }

    @Override
    protected MainActivityContract getContract() {
        return contract;
    }

    @Override
    protected KnitNavigator getNavigator() {
        return navigator;
    }

    @Override
    protected Class<MainPresenter> getPresenterClass() {
        return MainPresenter.class;
    }

    @Override
    public Knit getKnitInstance() {
        return knit;
    }

    @Override
    public InternalModel getModelManager() {
        return modelManager;
    }

    @Test
    public void onViewAppliedTest() throws Exception {
        verifyForDataRequestForTest();
    }

    @Test
    public void onHandleViewEventsTest() throws Exception{
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Class<? extends Activity>> targetCaptor = ArgumentCaptor.forClass(Class.class);
        MockEvents.fireMockClickEvent("button",mainPresenter);
        verify(contract).recMes(messageCaptor.capture());
        assertEquals("BUTTON PRESSED",messageCaptor.getValue());
        verify(navigator).toActivity();
        verify(activityNavigator).target(targetCaptor.capture());
        assertEquals(SecondActivity.class,targetCaptor.getValue());
        verifyForDataRequestForTest();
    }


    @Test
    public void updateData1Test() throws Exception{
        KnitResponse<String> response = MockResponses
                .builder(String.class)
                .setBody("TEST")
                .build();

       // mainPresenter.up(response);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(contract).recMes(messageCaptor.capture());
        assertEquals("TEST",messageCaptor.getValue());
    }


    private void verifyForDataRequestForTest(){
        ArgumentCaptor<String> dataRequestCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<InternalPresenter> presenterCaptor = ArgumentCaptor.forClass(InternalPresenter.class);
        ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);
        mainPresenter.onViewApplied(null,null);
      //  verify(modelManager,atLeastOnce()).request(dataRequestCaptor.capture(),presenterCaptor.capture(),paramsCaptor.capture());
        assertEquals(NULL_PRESENTER,presenterCaptor.getValue());
        assertEquals("test",dataRequestCaptor.getValue());
    }


}
