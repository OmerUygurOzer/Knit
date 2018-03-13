package com.omerozer.knit;

import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.classloaders.KnitPresenterLoader;
import com.omerozer.knit.classloaders.KnitUtilsLoader;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.schedulers.SchedulerProvider;

/**
 * Created by bo_om on 3/12/2018.
 */

public interface KnitInterface {

    SchedulerProvider getSchedulerProvider();

    ModelManager getModelManager();

    KnitNavigator getNavigator();

    KnitModelLoader getModelLoader();

    KnitPresenterLoader getPresenterLoader();

    KnitUtilsLoader getUtilsLoader();

}
