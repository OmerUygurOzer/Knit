package com.omerozer.knit;

/**
 * Created by omerozer on 2/6/18.
 */

public interface ViewToPresenterMapInterface {
    String getPresenterClass(String view);

    KnitPresenter createPresenterForView(Object viewObject,KnitModel modelManager);
}
