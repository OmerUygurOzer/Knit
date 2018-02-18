package com.omerozer.knitprocessor.model;

import static com.omerozer.knitprocessor.KnitFileStrings.*;

import com.omerozer.knitprocessor.KnitFileStrings;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

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


        TypeSpec.Builder modelMapBuilder = TypeSpec
                .classBuilder("ModelMap")
                .addSuperinterface(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL_MAP_INTERFACE))
                .addModifiers(Modifier.PUBLIC);

        WildcardTypeName wildcardTypeName = WildcardTypeName.subtypeOf(
                ClassName.bestGuess(KnitFileStrings.KNIT_MODEL));

        ParameterizedTypeName classTypeName = ParameterizedTypeName.get(TYPE_NAME_CLASS, wildcardTypeName);

        ParameterizedTypeName returnTypeForGetAll = ParameterizedTypeName.get(TYPE_NAME_LIST, classTypeName);

        int c = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("java.util.Arrays.asList(");
        for (KnitModelMirror modelMirror : modelMirrors) {
            stringBuilder.append(modelMirror.enclosingClass.getQualifiedName());
            stringBuilder.append("_Model");
            stringBuilder.append(".class");

            if (c < modelMirrors.size() - 1) {
                stringBuilder.append(",");
            }
            c++;
        }
        stringBuilder.append(")");

        MethodSpec getAllMethod = MethodSpec
                .methodBuilder("getAll")
                .addAnnotation(Override.class)
                .returns(returnTypeForGetAll)
                .addStatement("return " + stringBuilder.toString())
                .addModifiers(Modifier.PUBLIC)
                .build();

        modelMapBuilder.addMethod(getAllMethod);

        ParameterizedTypeName returnTypeForGeneratedVals = ParameterizedTypeName.get(TYPE_NAME_LIST, TYPE_NAME_STRING);

        MethodSpec.Builder getGeneratedValuesMethodBuilder = MethodSpec
                .methodBuilder("getGeneratedValues")
                .addParameter(TYPE_NAME_CLASS,"clazz")
                .addAnnotation(Override.class)
                .returns(returnTypeForGeneratedVals)
                .addModifiers(Modifier.PUBLIC);

        for(KnitModelMirror knitModelMirror: modelMirrors) {
            c = 0;
            stringBuilder = new StringBuilder();
            stringBuilder.append("java.util.Arrays.asList(");
            for (String field : knitModelMirror.vals) {
                stringBuilder.append("\"");
                stringBuilder.append(field);
                stringBuilder.append("\"");
                if (c < knitModelMirror.vals.size() - 1) {
                    stringBuilder.append(",");
                }
                c++;
            }
            stringBuilder.append(")");

            getGeneratedValuesMethodBuilder.beginControlFlow("if(clazz.equals($L$L.class))",knitModelMirror.enclosingClass.getQualifiedName(),"_Model");
            getGeneratedValuesMethodBuilder.addStatement("return $L",stringBuilder.toString());
            getGeneratedValuesMethodBuilder.endControlFlow();
        }

        getGeneratedValuesMethodBuilder.addStatement("return null");



        modelMapBuilder.addMethod(getGeneratedValuesMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder(KnitFileStrings.KNIT_PACKAGE,
                modelMapBuilder.build()).build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
