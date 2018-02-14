package com.omerozer.knit;

import com.omerozer.knit.generators.Callback;

/**
 * Created by omerozer on 2/3/18.
 */

public interface InternalModel extends ModelInterface {
    void request(String data,InternalPresenter presenter,Object... params);
    void request(String data,Callback callback,Object... params);
    KnitModel getParent();
    String[] getHandledValues();
}
