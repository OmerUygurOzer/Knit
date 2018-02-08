package com.omerozer.knit;

import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;
import com.omerozer.knit.viewevents.handlers.EventHandler;

/**
 * Created by omerozer on 2/1/18.
 */


public abstract class KnitPresenter implements MemoryEntity,EventHandler {

    public static final KnitPresenter THIS = new KnitPresenter() {
        @Override
        public void handle(ViewEventPool eventPool, ViewEventEnv eventEnv, KnitModel modelManager) {

        }

        @Override
        public void apply(Object viewObject) {

        }

        @Override
        public void releaseCurrentView() {

        }

        @Override
        public KnitModel getModelManager() {
            return null;
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

    public abstract KnitModel getModelManager();


}
