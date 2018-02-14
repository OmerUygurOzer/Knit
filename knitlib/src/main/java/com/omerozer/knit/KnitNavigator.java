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

/**
 * Created by omerozer on 2/13/18.
 */

public class KnitNavigator {

    public static ActivityNavigator toActivity(){
        return new ActivityNavigator();
    }

    public static FragmentNavigator toFragment(){
        return new FragmentNavigator();
    }

    static abstract class Navitator{
       public abstract Class<?> getTarget();
    }

    public static class ActivityNavigator extends Navitator{
        private WeakReference<Context> contextWeakReference;
        private Class<? extends Activity> target;
        private Bundle bundle;

        public ActivityNavigator setContext(Context context){
            this.contextWeakReference = new WeakReference<Context>(context);
            return this;
        }

        public ActivityNavigator toActivity(Class<? extends Activity> target){
            this.target = target;
            return this;
        }

        public ActivityNavigator addData(Bundle bundle){
            this.bundle = bundle;
            return this;
        }

        public void go(){
            Intent intent = new Intent(this.contextWeakReference.get(),target);
            Knit.setDataForNavigation(this,bundle);
            contextWeakReference.get().startActivity(intent);
        }

        @Override
        public Class<?> getTarget() {
            return target;
        }
    }

    public static class FragmentNavigator extends Navitator{
        private WeakReference<Context> contextWeakReference;
        private Class<? extends Fragment> target;
        private Bundle bundle;
        private FragmentManager fragmentManager;
        private int container;

        public FragmentNavigator setContext(Context context){
            this.contextWeakReference = new WeakReference<Context>(context);
            return this;
        }

        public FragmentNavigator addData(Bundle bundle){
            this.bundle = bundle;
            return this;
        }

        public FragmentNavigator toFragment(Class<? extends Fragment> target){
            this.target = target;
            return this;
        }

        public FragmentNavigator intoView(int id){
            this.container = id;
            return this;
        }

        public FragmentNavigator usingManager(FragmentManager fragmentManager){
            this.fragmentManager = fragmentManager;
            return this;
        }

        public void go(){
            Constructor constructor;
            Fragment instance = null;
            try {
                constructor = target.getConstructor();
                instance = (Fragment) constructor.newInstance();
                fragmentManager.beginTransaction()
                        .replace(container,instance)
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
