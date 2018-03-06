package com.omerozer.knit;

import com.omerozer.knit.viewevents.handlers.EventHandler;

/**
 * Created by omerozer on 2/1/18.
 */


public abstract class InternalPresenter implements EventHandler,PresenterInterface {

    public abstract InternalModel getModelManager();

    public abstract KnitNavigator getNavigator();

    public abstract Object getContract();

    public abstract String[] getUpdatableFields();

    public abstract KnitPresenter getParent();

}
