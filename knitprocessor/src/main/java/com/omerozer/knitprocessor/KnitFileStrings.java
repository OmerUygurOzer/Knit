package com.omerozer.knitprocessor;

import com.squareup.javapoet.ClassName;

import java.util.Collection;
import java.util.List;

/**
 * Created by omerozer on 2/3/18.
 */

public class KnitFileStrings {

    public static final String KNIT_PACKAGE = "com.omerozer.knit";
    public static final String KNIT = "com.omerozer.knit.Knit";
    public static final String KNIT_CLASS_COMMENT = "This class has been generated by Knit. Do NOT alter it's contents.";

    //PRESENTER
    public static final String KNIT_PRESENTER = "com.omerozer.knit.InternalPresenter";
    public static final String KNIT_PRESENTER_POSTFIX = "_Presenter";
    public static final String KNIT_PRESENTER_UPDATE_METHOD_POSTFIX = "_Update";
    public static final String KNIT_PRESENTER_GET_MODEL_MANAGER_METHOD = "getModelManager";

    public static final String KNIT_PRESENTER_GET_VIEW_METHOD = "getContract";
    public static final String KNIT_GET_UPDATEABLES_METHOD = "getUpdatableFields";
    public static final String KNIT_PRESENTER_APPLY_METHOD = "onViewApplied";
    public static final String KNIT_PRESENTER_RELEASE_METHOD = "onCurrentViewReleased";
    public static final String KNIT_PRESENTER_GET_PARENT_METHOD = "getParent";
    public static final String KNIT_PRESENTER_RECEIVE_MESSAGE = "receiveMessage";

    //MEMORY ENTITY
    public static final String KNIT_ME_ONCREATE_METHOD = "onCreate";
    public static final String KNIT_ME_LOAD_METHOD = "onLoad";
    public static final String KNIT_ME_DESTROY_METHOD = "onDestroy";
    public static final String KNIT_ME_SHOULD_LOAD_METHOD = "shouldLoad";
    public static final String KNIT_ME_MEMORY_LOW_METHOD = "onMemoryLow";

    //MODEL
    public static final String KNIT_MODEL = "com.omerozer.knit.InternalModel";
    public static final String KNIT_MODEL_EXT = "com.omerozer.knit.KnitModel";
    public static final String KNIT_MODEL_POSTFIX = "_Model";
    public static final String KNIT_MODEL_REQUEST_METHOD = "request";
    public static final String KNIT_MODEL_EXPOSER_POSTFIX = "_Exposer";
    public static final String KNIT_MODEL_GETHANDLEDVALUES = "getHandledValues";
    public static final String KNIT_MODEL_GET_PARENT_METHOD = "getParent";
    public static final String KNIT_MODEL_INPUT_METHOD = "input";
    public static final String KNIT_GET_NAVIGATOR_METHOD = "getNavigator";
    public static final String KNIT_MODEL_IS_SINGLETON = "isSingleton";

    //USER
    public static final String KNIT_USER = "com.omerozer.knit.KnitUser";
    public static final String KNIT_PRESENTER_USER_POSTFIX = "_User";

    //UTILS
    public static final String KNIT_ASYNC_TASK = "com.omerozer.knit.KnitAsyncTaskHandler";
    public static final String KNIT_MODEL_MAP_INTERFACE = "com.omerozer.knit.ModelMapInterface";
    public static final String KNIT_MODEL_MAP = "com.omerozer.knit.ModelMap";
    public static final String KNIT_VIEW_PRESENTER = "ViewToPresenterMap";
    public static final String KNIT_VIEW_PRESENTER_INTERFACE = "com.omerozer.knit.ViewToPresenterMapInterface";
    public static final String KNIT_EVENT_HANDLER = "com.omerozer.knit.viewevents.handlers.EventHandler";
    public static final String KNIT_EVENT_HANDLE_METHOD = "handle";
    public static final String KNIT_EVENT_VIEW_EVENT_POOL = "com.omerozer.knit.viewevents.ViewEventPool";
    public static final String KNIT_EVENT_VIEW_EVENT_ENV = "com.omerozer.knit.viewevents.ViewEventEnv";
    public static final String KNIT_CALLBACK = "com.omerozer.knit.generators.Callback";
    public static final String KNIT_SUBMITTER = "com.omerozer.knit.collectors.Submitter";
    public static final String KNIT_RESPONSE = "com.omerozer.knit.KnitResponse";
    public static final String KNIT_NAVIGATOR = "com.omerozer.knit.KnitNavigator";
    public static final String KNIT_TASK_FLOW = "com.omerozer.knit.schedulers.KnitTaskFlow";
    public static final String KNITSCHEDULER_IO = "io()";
    public static final String KNITSCHEDULER_MAIN = "main()";
    public static final String KNITSCHEDULER_IMMEDIATE = "immediate()";
    public static final String KNITSCHEDULER_HEAVY = "heavy()";
    public static final String KNIT_MODEL_REQUEST_IMMEDIATE_METHOD = "requestImmediately";

    public static final ClassName TYPE_NAME_CLASS = ClassName.bestGuess(Class.class.getCanonicalName());
    public static final ClassName TYPE_NAME_STRING = ClassName.bestGuess(String.class.getCanonicalName());
    public static final ClassName TYPE_NAME_LIST = ClassName.bestGuess(List.class.getCanonicalName());
    public static final ClassName TYPE_NAME_SCHEDULER_PROVIDER = ClassName.bestGuess("com.omerozer.knit.schedulers.SchedulerProvider");
    public static final ClassName TYPE_NAME_CALLABLE = ClassName.bestGuess("java.util.concurrent.Callable");
    public static final ClassName TYPE_NAME_CONSUMER = ClassName.bestGuess("com.omerozer.knit.schedulers.Consumer");
    public static final ClassName TYPE_NAME_SCHEDULER_ENUM = ClassName.bestGuess("com.omerozer.knit.schedulers.KnitSchedulers");
    public static final ClassName TYPE_NAME_KNIT_MESSAGE = ClassName.bestGuess("com.omerozer.knit.KnitMessage");


    //CONTRACT
    public static final String KNIT_CONTRACT_POSTFIX = "Contract";

    //ANDROID
    public static final String ANDROID_HANDLER = "android.os.Handler";
    public static final String ANDROID_LOOPER = "android.os.Looper";
    public static final String ANDROID_BUNDLE = "android.os.Bundle";
    public static final ClassName ANDROID_INTENT = ClassName.bestGuess("android.content.Intent");


    public static final String STRING_CLASS = String.class.getCanonicalName();



    public static String createStringArrayField(Collection<String> strings){
        String[] stringsArray = new String[strings.size()];

        StringBuilder builder = new StringBuilder();
        builder.append("new ");
        builder.append(STRING_CLASS);
        builder.append("[]{");

        int c = 0;

        for(String event : strings){
            stringsArray[c++] = event;
        }

        for(int i = 0 ;i < strings.size(); i++){
            builder.append("\"");
            builder.append(stringsArray[i]);
            builder.append("\"");
            if(i!=stringsArray.length-1){
                builder.append(",");
            }
        }
        builder.append("}");
        return builder.toString();
    }

}
