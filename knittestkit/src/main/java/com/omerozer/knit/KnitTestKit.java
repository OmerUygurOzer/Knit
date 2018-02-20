package com.omerozer.knit;

import com.omerozer.knit.classloaders.KnitUtilsLoader;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by omerozer on 2/18/18.
 */

public class KnitTestKit {

    public  static<T extends KnitPresenter> PresenterBuilder<T> presenterBuilder(Class<T> presenterClass){
        return new PresenterBuilder<T>().from(presenterClass);
    }

    public static class PresenterBuilder<T extends KnitPresenter>{

        private Class<T> clazz;
        private Knit knit;
        private InternalModel modelManager;
        private KnitNavigator navigator;
        private Object contract;

        PresenterBuilder<T> from(Class<T> clazz){
            this.clazz = clazz;
            return this;
        }

        public PresenterBuilder<T> setKnit(Knit knit){
            this.knit = knit;
            return this;
        }

        public PresenterBuilder<T> setModelManager(InternalModel internalModel){
            this.modelManager = internalModel;
            return this;
        }

        public PresenterBuilder<T> setNavigator(KnitNavigator navigator){
            this.navigator = navigator;
            return this;
        }

        public PresenterBuilder<T> usingContract(Object contract){
            this.contract = contract;
            return this;
        }

        public T build(){
            try {
                T presenter = (T)clazz.getConstructor().newInstance();
                presenter.setKnit(knit);
                presenter.setContract(contract);
                presenter.setModelManager(modelManager);
                presenter.setNavigator(navigator);
                return presenter;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


}
