package com.omerozer.knit;

/**
 * Created by omerozer on 2/3/18.
 */

public interface InternalModel extends ModelInterface {
    void request(String data,InternalPresenter presenter,Object... params);
    String[] getHandledValues();

}
