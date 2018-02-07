package com.omerozer.knitprocessor.model;

import com.omerozer.knitprocessor.KnitFileStrings;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Created by omerozer on 2/6/18.
 */

public class ModelMapWriter {
    static void write(Filer filer, Set<KnitModelMirror> modelMirrors) {

        String[] models = new String[modelMirrors.size()];
        int i = 0;
        for(KnitModelMirror knitModelMirror: modelMirrors){
            models[i++] = "("+ KnitFileStrings.KNIT_MODEL + ")"+"new " + knitModelMirror.enclosingClass.getQualifiedName() + KnitFileStrings.KNIT_MODEL_POSTFIX+"("  +"new " +knitModelMirror.enclosingClass.getQualifiedName() +"()"+  ",asyncTaskHandler)";
        }

        TypeSpec.Builder modelMapBuilder = TypeSpec
                .classBuilder("ModelMap")
                .addSuperinterface(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL_MAP_INTERFACE))
                .addModifiers(Modifier.PUBLIC);


        ParameterizedTypeName returnType = ParameterizedTypeName.get(
                ClassName.bestGuess(List.class.getCanonicalName()),
                ClassName.bestGuess(KnitFileStrings.KNIT_MODEL));

        int c = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("java.util.Arrays.asList(");
        for(KnitModelMirror modelMirror: modelMirrors){
            stringBuilder.append("$L");

            if(c<modelMirrors.size()-1){
                stringBuilder.append(",");
            }
            c++;
        }
        stringBuilder.append(")");

        MethodSpec getAllMethod = MethodSpec
                .methodBuilder("getAll")
                .addAnnotation(Override.class)
                .returns(returnType)
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_ASYNC_TASK),"asyncTaskHandler")
                .addStatement("return "+stringBuilder.toString(),models)
                .addModifiers(Modifier.PUBLIC)
                .build();

        modelMapBuilder.addMethod(getAllMethod);

        JavaFile javaFile = JavaFile.builder(KnitFileStrings.KNIT_PACKAGE,modelMapBuilder.build()).build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
