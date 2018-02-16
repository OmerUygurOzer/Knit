package com.omerozer.knitprocessor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

/**
 * Created by omerozer on 2/15/18.
 */

public class InterfaceMethodsCreatorForExposers {

    public static MethodSpec getOnCreateMethod(){
        return MethodSpec
                .methodBuilder("use_onCreate")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("parent.onCreate()")
                .build();
    }

    public static MethodSpec getOnViewAppliedMethod(){
        return MethodSpec
                .methodBuilder("use_onViewApplied")
                .addParameter(Object.class,"o")
                .addParameter(ClassName.bestGuess(KnitFileStrings.ANDROID_BUNDLE),"data")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("parent.onViewApplied(o,data)")
                .build();
    }

    public static MethodSpec getOnCurrentViewReleased(){
        return MethodSpec
                .methodBuilder("use_onCurrentViewReleased")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("parent.onCurrentViewReleased()")
                .build();
    }
}
