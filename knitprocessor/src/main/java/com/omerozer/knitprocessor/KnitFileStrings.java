package com.omerozer.knitprocessor;

import java.util.Collection;

/**
 * Created by omerozer on 2/3/18.
 */

public class KnitFileStrings {
    public static final String KNIT_PACKAGE = "com.omerozer.knit";
    public static final String KNIT = "com.omerozer.knit.Knit";

    //PRESENTER
    public static final String KNIT_PRESENTER = "com.omerozer.knit.KnitPresenter";
    public static final String KNIT_PRESENTER_POSTFIX = "_Presenter";
    public static final String KNIT_PRESENTER_LOAD_METHOD = "load";
    public static final String KNIT_PRESENTER_DESTROY_METHOD = "destroy";
    public static final String KNIT_PRESENTER_SHOULD_LOAD_METHOD = "shouldLoad";
    public static final String KNIT_PRESENTER_APPLY_METHOD = "apply";
    public static final String KNIT_PRESENTER_RELEASE_METHOD = "releaseCurrentView";
    public static final String KNIT_PRESENTER_UPDATE_METHOD_POSTFIX = "_Update";
    public static final String KNIT_PRESENTER_SEED_FIELD_POSTFIX = "_Seed";
    public static final String KNIT_PRESENTER_MUTATOR_FIELD_POSTFIX = "_Mutator";

    //MODEL
    public static final String KNIT_MODEL = "com.omerozer.knit.KnitModel";
    public static final String KNIT_MODEL_POSTFIX = "_Model";
    public static final String KNIT_MODEL_REQUEST_METHOD = "request";
    public static final String KNIT_MODEL_EXPOSER_POSTFIX = "_Exposer";
    public static final String KNIT_MODEL_GETHANDLEDVALUES = "getHandledValues";

    //USER
    public static final String KNIT_USER = "com.omerozer.knit.KnitUser";
    public static final String KNIT_PRESENTER_USER_POSTFIX = "_User";

    //UTILS
    public static final String KNIT_ASYNC_TASK = "com.omerozer.knit.KnitAsyncTaskHandler";
    public static final String KNIT_MODEL_MAP_INTERFACE = "com.omerozer.knit.ModelMapInterface";
    public static final String KNIT_MODEL_MAP = "com.omerozer.knit.ModelMap";
    public static final String KNIT_VIEW_PRESENTER = "ViewToPresenterMap";
    public static final String KNIT_VIEW_PRESENTER_INTERFACE = "com.omerozer.knit.ViewToPresenterMapInterface";

    //ANDROID
    public static final String ANDROID_HANDLER = "android.os.Handler";
    public static final String ANDROID_LOOPER = "android.os.Looper";


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
