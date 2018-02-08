package com.omerozer.knit.viewevents;

import android.os.Bundle;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by omerozer on 2/7/18.
 */

public abstract class ViewEventEnv {

    private String tag;

    private Bundle dataBundle;

    private WeakReference<View> viewWeakReference;

    protected ViewEventEnv(String tag,View view){
        this();
        this.tag = tag;
        this.viewWeakReference = new WeakReference<View>(view);
    }

    protected ViewEventEnv(){
        this.dataBundle = new Bundle();
    }

    public void clear(){
        this.tag = null;
        this.dataBundle.clear();
        this.viewWeakReference = null;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setViewWeakReference(View view) {
        this.viewWeakReference = new WeakReference<View>(view);
    }

    public View getView(){
        return viewWeakReference.get();
    }

    public String getTag() {
        return tag;
    }

    public Bundle getDataBundle() {
        return dataBundle;
    }
}
