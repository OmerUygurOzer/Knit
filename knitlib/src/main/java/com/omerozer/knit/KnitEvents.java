package com.omerozer.knit;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.omerozer.knit.viewevents.KnitOnClickEvent;
import com.omerozer.knit.viewevents.KnitOnClickEventPool;
import com.omerozer.knit.viewevents.KnitOnFocusChangedEvent;
import com.omerozer.knit.viewevents.KnitOnFocusChangedEventPool;
import com.omerozer.knit.viewevents.KnitOnRefreshEvent;
import com.omerozer.knit.viewevents.KnitOnTextChangedEventPool;
import com.omerozer.knit.viewevents.KnitSwipeRefreshLayoutEventPool;
import com.omerozer.knit.viewevents.KnitTextChangedEvent;

/**
 * Created by omerozer on 2/2/18.
 */

public class KnitEvents {

    private static Knit knitInstance;

    static void init(Knit knit){
        knitInstance = knit;
    }

    private final static KnitOnClickEventPool onClickEventPool = new KnitOnClickEventPool();
    private final static KnitOnTextChangedEventPool onTextChangedEventPool = new KnitOnTextChangedEventPool();
    private final static KnitOnFocusChangedEventPool onFocusChangedEventPool = new KnitOnFocusChangedEventPool();
    private final static KnitSwipeRefreshLayoutEventPool onSwipeRefreshEventPool = new KnitSwipeRefreshLayoutEventPool();

    public static void onClick(final String tag, final Object carrierObject, View view) {
        view.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                KnitOnClickEvent event = onClickEventPool.getEvent();
                event.setTag(tag);
                event.setViewWeakReference(view);
                knitInstance.findPresenterForView(carrierObject).handle(onClickEventPool, event, knitInstance.getModelManager());
            }
        });
    }

    public static void onTextChanged(final String tag, final Object carrierObject,
            final EditText view) {
        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                view.removeTextChangedListener(this);
                KnitTextChangedEvent event = onTextChangedEventPool.getEvent();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.BEFORE);
                event.setCharSequence(charSequence);
                event.setI(i);
                event.setI1(i1);
                event.setI2(i2);
                knitInstance.findPresenterForView(carrierObject).handle(onTextChangedEventPool, event,
                        knitInstance.getModelManager());
                view.addTextChangedListener(this);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                view.removeTextChangedListener(this);
                KnitTextChangedEvent event = onTextChangedEventPool.getEvent();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.ON);
                event.setCharSequence(charSequence);
                event.setI(i);
                event.setI1(i1);
                event.setI2(i2);
                knitInstance.findPresenterForView(carrierObject).handle(onTextChangedEventPool, event,
                        knitInstance.getModelManager());
                view.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                view.removeTextChangedListener(this);
                KnitTextChangedEvent event = onTextChangedEventPool.getEvent();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.AFTER);
                event.setAfterEditable(editable);
                knitInstance.findPresenterForView(carrierObject).handle(onTextChangedEventPool, event,
                        knitInstance.getModelManager());
                view.addTextChangedListener(this);
            }
        };
        view.addTextChangedListener(watcher);
    }

    public static void onFocusChanged(final String tag, final Object carrierObject,
            final View view) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                KnitOnFocusChangedEvent event = onFocusChangedEventPool.getEvent();
                event.setTag(tag);
                event.setFocus(b);
                knitInstance.findPresenterForView(carrierObject).handle(onFocusChangedEventPool, event, knitInstance.getModelManager());
            }
        });
    }

    public static void onSwipeRefresh(final String tag, final Object carrierObject,final SwipeRefreshLayout view) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KnitOnRefreshEvent event = onSwipeRefreshEventPool.getEvent();
                event.setTag(tag);
                event.setViewWeakReference(view);
                knitInstance.findPresenterForView(carrierObject).handle(onSwipeRefreshEventPool, event, knitInstance.getModelManager());
            }
        });
    }

}
