package com.omerozer.knit;

import android.os.Bundle;

import com.omerozer.knit.schedulers.KnitSchedulers;
import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;

/**
 * Created by omerozer on 2/19/18.
 */

public final class NullValues {

    public static final Object[] NULL_PARAMS = new Object[0];

    public static final InternalModel NULL_MODEL = new InternalModel() {

        @Override
        public void request(String data, KnitSchedulers runOn, KnitSchedulers consumeOn,
                InternalPresenter presenter, Object... params) {

        }

        @Override
        public <T> KnitResponse<T> requestImmediately(String data, Object... params) {
            return null;
        }

        @Override
        public void input(String data, Object... params) {

        }

        @Override
        public KnitModel getParent() {
            return null;
        }

        @Override
        public String[] getHandledValues() {
            return new String[0];
        }
    };

    public static final InternalPresenter NULL_PRESENTER = new InternalPresenter() {
        @Override
        public InternalModel getModelManager() {
            return null;
        }

        @Override
        public KnitNavigator getNavigator() {
            return null;
        }

        @Override
        public Object getContract() {
            return null;
        }

        @Override
        public String[] getUpdatableFields() {
            return new String[0];
        }

        @Override
        public void onViewApplied(Object viewObject, Bundle bundle) {

        }

        @Override
        public void onCurrentViewReleased() {

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onLoad() {

        }

        @Override
        public void onMemoryLow() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public boolean shouldLoad() {
            return false;
        }

        @Override
        public void handle(ViewEventPool eventPool, ViewEventEnv eventEnv,
                InternalModel modelManager) {

        }
    };
}
