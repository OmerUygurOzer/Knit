package com.omerozer.knit;

/**
 * Created by omerozer on 2/1/18.
 */


public abstract class KnitPresenter implements MemoryEntity {

    public static final KnitPresenter THIS = new KnitPresenter() {
        @Override
        public void apply(Object viewObject) {

        }

        @Override
        public void releaseCurrentView() {

        }

        @Override
        public void load() {

        }

        @Override
        public void destroy() {

        }

        @Override
        public boolean shouldLoad() {
            return false;
        }
    };

    public abstract void apply(Object viewObject);

    public abstract void releaseCurrentView();

}
