package com.omerozer.knit.viewevents.handlers;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;

/**
 * Created by omerozer on 2/7/18.
 */

public interface EventHandler {
    void handle(ViewEventPool eventPool,ViewEventEnv eventEnv,InternalModel modelManager);
}
