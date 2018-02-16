package com.omerozer.knitprocessor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;

/**
 * Created by omerozer on 2/15/18.
 */

public class KnitMethodsFilter {
    static final Set<String> knitSpecificMethods = new HashSet<>();

    static {
        knitSpecificMethods.addAll(
                Arrays.asList("onCreate","onDestroy","onViewApplied","onCurrentViewReleased")
        );
    }


    public static boolean filter(Element element){
        return !knitSpecificMethods.contains(element.getSimpleName().toString());
    }
}
