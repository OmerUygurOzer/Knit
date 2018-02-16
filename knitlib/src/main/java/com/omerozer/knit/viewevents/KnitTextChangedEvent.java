package com.omerozer.knit.viewevents;

import android.text.Editable;
import android.view.View;

/**
 * Created by omerozer on 2/7/18.
 */

public class KnitTextChangedEvent extends ViewEventEnv {

    public enum State {
        BEFORE,
        ON,
        AFTER;
    }

    private static final String I = "i";
    private static final String I1 = "i1";
    private static final String I2 = "i2";

    private State state;

    private CharSequence charSequence;

    private Editable afterEditable;

    public KnitTextChangedEvent(String tag, View view, State state, CharSequence charSequence ,Editable editable) {
        super(tag, view);
        this.state = state;
        this.afterEditable = editable;
        this.charSequence = charSequence;
    }

    public KnitTextChangedEvent() {
        super();
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setCharSequence(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    public void setAfterEditable(Editable afterEditable) {
        this.afterEditable = afterEditable;
    }

    public CharSequence getCharSequence() {
        return charSequence;
    }

    public Editable getAfterEditable() {
        return afterEditable;
    }

    public State getState() {
        return state;
    }

    public int getI(){
        return getDataBundle().getInt(I);
    }

    public int getI1(){
        return getDataBundle().getInt(I1);
    }

    public int getI2(){
        return getDataBundle().getInt(I2);
    }

    public void setI(int i){
        getDataBundle().putInt(I,i);
    }

    public void setI1(int i1){
        getDataBundle().putInt(I1,i1);
    }

    public void setI2(int i2){
        getDataBundle().putInt(I2,i2);
    }


}
