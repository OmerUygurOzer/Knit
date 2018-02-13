package com.omerozer.knit;

/**
 * Created by omerozer on 2/6/18.
 */

public interface ViewToPresenterMapInterface {
    String getPresenterClass(String view);

    InternalPresenter createPresenterForView(Object viewObject,InternalModel modelManager);

    InternalPresenter getPresenterForParent(Object parentObject);
}
