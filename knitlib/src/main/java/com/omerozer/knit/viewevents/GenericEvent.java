package com.omerozer.knit.viewevents;

/**
 * Created by Omer Ozer on 3/13/2018.
 */

public class GenericEvent extends ViewEventEnv {

    private Object[] params;

    public GenericEvent(String tag,Object... data) {
        this.params = data;
    }

    public GenericEvent(){}

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object... params) {
        this.params = params;
    }

    public Object[] getData(){
        return params;
    }

}
