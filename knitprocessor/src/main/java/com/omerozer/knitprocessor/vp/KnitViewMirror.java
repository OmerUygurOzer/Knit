package com.omerozer.knitprocessor.vp;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 2/2/18.
 */

class KnitViewMirror {

    public TypeElement enclosingClass;

    public Map<String,VariableElement> leechingFields = new LinkedHashMap<>();

    public Map<String,ExecutableElement> updatingMethods = new LinkedHashMap<>();

}
