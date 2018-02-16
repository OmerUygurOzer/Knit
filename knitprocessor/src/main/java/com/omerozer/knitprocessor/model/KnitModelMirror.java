package com.omerozer.knitprocessor.model;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 2/4/18.
 */

class KnitModelMirror {

    public TypeElement enclosingClass;

    public Map<String[],VariableElement> generateField = new LinkedHashMap<>();

    public Map<String[],VariableElement> generateAsyncField = new LinkedHashMap<>();

    public Map<String[],VariableElement> collectorField = new LinkedHashMap<>();

    public Map<String[],VariableElement> inputterField = new LinkedHashMap<>();

    public Set<String> vals = new LinkedHashSet<>();

}
