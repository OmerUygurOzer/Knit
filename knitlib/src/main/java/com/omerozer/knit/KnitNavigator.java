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

    public FragmentNavigator toFragment() {
        return new FragmentNavigator();
    }

    private Map<Class<?>, Bundle> navigatorDataMap = new LinkedHashMap<>();

    public Bundle getDataForTarget(Object object) {
        return navigatorDataMap.containsKey(object.getClass()) ? navigatorDataMap.get(
                object.getClass()) : null;
    }

    void navigatedTo(Object viewObject) {
        if (viewObject instanceof Activity) {
            contextRef = new WeakReference<Context>((Activity) viewObject);
        }
    }


    abstract class Navigator {
        public abstract Class<?> getTarget();
    }

    public class ActivityNavigator extends Navigator {
        private Class<? extends Activity> target;
        private Bundle bundle;

        public ActivityNavigator target(Class<? extends Activity> target) {
            this.target = target;
            return this;
        }

        public ActivityNavigator addData(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public void go() {
            Intent intent = new Intent(contextRef.get(), target);
            if (bundle != null) {
                navigatorDataMap.put(target, bundle);
            }
            contextRef.get().startActivity(intent);
        }

        @Override
        public Class<?> getTarget() {
            return target;
        }
    }

    public class FragmentNavigator extends Navigator {
        private Class<? extends Fragment> target;
        private Bundle bundle;
        private FragmentManager fragmentManager;
        private int container;

        public FragmentNavigator addData(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public FragmentNavigator target(Class<? extends Fragment> target) {
            this.target = target;
            return this;
        }

        public FragmentNavigator intoView(int id) {
            this.container = id;
            return this;
        }

        public FragmentNavigator usingManager(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
            return this;
        }

        public void go() {
            Constructor constructor;
            Fragment instance = null;
            try {
                constructor = target.getConstructor();
                instance = (Fragment) constructor.newInstance();
                fragmentManager.beginTransaction()
                        .replace(container, instance)
                        .commit();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }


        @Override
        public Class<?> getTarget() {
            return target;
        }
    }


}
