package com.omerozer.knit;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.omerozer.knit.viewevents.KnitOnClickEvent;
import com.omerozer.knit.viewevents.KnitOnClickEventPool;
import com.omerozer.knit.viewevents.KnitOnFocusChangedEvent;
import com.omerozer.knit.viewevents.KnitOnFocusChangedEventPool;
import com.omerozer.knit.viewevents.KnitOnTextChangedEventPool;
import com.omerozer.knit.viewevents.KnitTextChangedEvent;

/**
 * Created by omerozer on 2/2/18.
 */

public class KnitEvents {

    private static KnitOnClickEventPool onClickEventPool       = new KnitOnClickEventPool();
    private static KnitOnTextChangedEventPool onTextChangedEventPool = new KnitOnTextChangedEventPool();
    private static KnitOnFocusChangedEventPool onFocusChangedEventPool = new KnitOnFocusChangedEventPool();

    public static void onClick(final String tag, final Object carrierObject,View view){
        view.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                KnitOnClickEvent event = onClickEventPool.getEvent();
                event.setTag(tag);
                event.setViewWeakReference(view);
                Knit.findPresenterForView(carrierObject).handle(onClickEventPool,event,Knit.getModelManager());
            }
        });
    }

    public static void onTextChanged(final String tag, final Object carrierObject, final EditText view){
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
                Knit.findPresenterForView(carrierObject).handle(onTextChangedEventPool,event,Knit.getModelManager());
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
                Knit.findPresenterForView(carrierObject).handle(onTextChangedEventPool,event,Knit.getModelManager());
                view.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                view.removeTextChangedListener(this);
                KnitTextChangedEvent event = onTextChangedEventPool.getEvent();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.AFTER);
                event.setAfterEditable(editable);
                Knit.findPresenterForView(carrierObject).handle(onTextChangedEventPool,event,Knit.getModelManager());
                view.addTextChangedListener(this);
            }
        };
        view.addTextChangedListener(watcher);
    }

    public static void onFocusChanged(final String tag, final Object carrierObject, final View view){
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                KnitOnFocusChangedEvent event = onFocusChangedEventPool.getEvent();
                event.setTag(tag);
                event.setFocus(b);
                Knit.findPresenterForView(carrierObject).handle(onFocusChangedEventPool,event,Knit.getModelManager());
            }
        });
    }



}
