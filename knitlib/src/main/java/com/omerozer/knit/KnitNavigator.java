package com.omerozer.knit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/13/18.
 */

public class KnitNavigator {

    private static KnitNavigator instance;

    static KnitNavigator getInstance() {
        if (instance == null) {
            instance = new KnitNavigator();
        }
        return instance;
    }

    private WeakReference<Context> contextRef;

    public ActivityNavigator toActivity() {
        return new ActivityNavigator();
    }

    private Map<Class<?>, Bundle> navigatorDataMap = new LinkedHashMap<>();

    public Bundle getDataForTarget(Object object) {
        return navigatorDataMap.containsKey(object.getClass()) ? navigatorDataMap.get(object.getClass()) : null;
    }

    void navigatedTo(Object viewObject) {
        if (viewObject instanceof Activity) {
            contextRef = new WeakReference<Context>((Activity) viewObject);
        }
    }


    public abstract class Navigator {
        public abstract Class<?> getTarget();
    }

    public class ActivityNavigator extends Navigator {
        private Class<? extends Activity> target;
        public ActivityNavigator target(Class<? extends Activity> target) {
            this.target = target;
            return this;
        }

        public void go() {
            Intent intent = new Intent(contextRef.get(), target);
            contextRef.get().startActivity(intent);
        }

        @Override
        public Class<?> getTarget() {
            return target;
        }
    }




}
