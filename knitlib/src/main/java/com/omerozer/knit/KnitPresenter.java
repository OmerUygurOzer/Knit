package com.omerozer.knit;

import com.omerozer.knit.viewevents.handlers.EventHandler;

/**
 * Created by omerozer on 2/12/18.
 */

public abstract class KnitPresenter<T> implements EventHandler,PresenterInterface{

    protected void requestData(String data,Object... params){
        InternalPresenter instance = Knit.findPresenterForParent(this);
        instance.getModelManager().request(data,instance,params);
    }

    protected T getView(){
        return (T) Knit.findPresenterForParent(this).getView();
    }


}
