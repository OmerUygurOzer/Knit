package com.omerozer.knitprocessor.vp;

import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by omerozer on 2/2/18.
 */

public class KnitPresenterMirror {

    public TypeMirror targetView;

    public TypeElement enclosingClass;

    public Map<String,VariableElement> seedingFields = new LinkedHashMap<>();

    public Map<String,VariableElement> mutatorFields = new LinkedHashMap<>();

    public Map<String,String[]> mutatorParams = new LinkedHashMap<>();

    public boolean isEnclosingAHandler = false;

    public Map<String,VariableElement> eventHandlerFields = new LinkedHashMap<>();


}
