package com.omerozer.knit;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by omerozer on 3/16/18.
 */

public class KnitBackStack {

    private Node base;

    private Node tail;

    public KnitBackStack(){

    }

    private class Node{
        WeakReference<Activity> activity;
        Node next;
    }

    public void addActivity(Activity activity){

    }

}
