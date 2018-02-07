package com.omerozer.knit;

/**
 * Created by omerozer on 2/1/18.
 */


public abstract class  KnitPresenter implements MemoryEntity {

     public abstract void apply(Object viewObject);

     public abstract void releaseCurrentView();

}
