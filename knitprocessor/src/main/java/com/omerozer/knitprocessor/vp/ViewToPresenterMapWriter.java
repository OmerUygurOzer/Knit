package com.omerozer.knitprocessor.vp;

import com.omerozer.knitprocessor.KnitFileStrings;
import com.omerozer.knitprocessor.PackageStringExtractor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Created by omerozer on 2/6/18.
 */

public class ViewToPresenterMapWriter {
    static void write(Filer filer, Map<KnitPresenterMirror, KnitViewMirror> map) {
        TypeSpec.Builder viewToPresenterMapBuilder = TypeSpec
                .classBuilder(KnitFileStrings.KNIT_VIEW_PRESENTER)
                .addSuperinterface(
                        ClassName.bestGuess(KnitFileStrings.KNIT_VIEW_PRESENTER_INTERFACE))
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder findPresenterForClassMethodBuilder = MethodSpec
                .methodBuilder("getPresenterClassForView")
                .addParameter(ClassName.bestGuess(Class.class.getCanonicalName()), "viewClass")
                .addModifiers(Modifier.PUBLIC)
                .returns(Class.class)
                .addAnnotation(Override.class);


        for (KnitPresenterMirror knitPresenterMirror : map.keySet()) {
            String packageString = PackageStringExtractor.extract(
                    map.get(knitPresenterMirror).enclosingClass.asType());
            findPresenterForClassMethodBuilder.beginControlFlow("if(viewClass.equals($L.class))",
                    map.get(knitPresenterMirror).enclosingClass.getQualifiedName());
            findPresenterForClassMethodBuilder.addStatement("return $L.$L_Presenter.class",
                    packageString, knitPresenterMirror.enclosingClass.getSimpleName());
            findPresenterForClassMethodBuilder.endControlFlow();
        }
        findPresenterForClassMethodBuilder.addStatement("return null");

        WildcardTypeName wildcardTypeName = WildcardTypeName.subtypeOf(TypeName.OBJECT);

        ParameterizedTypeName viewClassName = ParameterizedTypeName.get(ClassName.bestGuess(Class.class.getCanonicalName()),wildcardTypeName);

        ParameterizedTypeName returnTypeForGetAll = ParameterizedTypeName.get(
                ClassName.bestGuess(List.class.getCanonicalName()), viewClassName);

        MethodSpec.Builder getAllViewsMethodBuilder = MethodSpec
                .methodBuilder("getAllViews")
                .addAnnotation(Override.class)
                .returns(returnTypeForGetAll)
                .addModifiers(Modifier.PUBLIC);

        int c = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("java.util.Arrays.asList(");
        for (KnitPresenterMirror modelMirror : map.keySet()) {
            stringBuilder.append(map.get(modelMirror).enclosingClass.getQualifiedName());
            stringBuilder.append(".class");
            if (c < map.size() - 1) {
                stringBuilder.append(",");
            }
            c++;
        }
        stringBuilder.append(",Object.class");
        stringBuilder.append(")");

        getAllViewsMethodBuilder.addStatement("return $L",stringBuilder.toString());

        viewToPresenterMapBuilder.addMethod(findPresenterForClassMethodBuilder.build());
        viewToPresenterMapBuilder.addMethod(getAllViewsMethodBuilder.build());


        ParameterizedTypeName returnTypeForGeneratedVals = ParameterizedTypeName.get(
                ClassName.bestGuess(List.class.getCanonicalName()),
                ClassName.bestGuess(String.class.getCanonicalName()));

        MethodSpec.Builder getUpdatingValuesMethodBuilder = MethodSpec
                .methodBuilder("getUpdatingValues")
                .addParameter(ClassName.bestGuess(Class.class.getCanonicalName()),"clazz")
                .addAnnotation(Override.class)
                .returns(returnTypeForGeneratedVals)
                .addModifiers(Modifier.PUBLIC);

        for(KnitPresenterMirror presenterMirror: map.keySet()) {
            c = 0;
            stringBuilder = new StringBuilder();
            stringBuilder.append("java.util.Arrays.asList(");
            String packageString = PackageStringExtractor.extract(map.get(presenterMirror).enclosingClass.asType());

            for (String field : presenterMirror.updatingMethodsMap.keySet()) {
                stringBuilder.append("\"");
                stringBuilder.append(field);
                stringBuilder.append("\"");
                if (c < presenterMirror.updatingMethodsMap.size() - 1) {
                    stringBuilder.append(",");
                }
                c++;
            }
            stringBuilder.append(")");

            getUpdatingValuesMethodBuilder.beginControlFlow("if(clazz.equals($L.$L$L.class))",packageString, presenterMirror.enclosingClass.getSimpleName(),"_Presenter");
            getUpdatingValuesMethodBuilder.addStatement("return $L",stringBuilder.toString());
            getUpdatingValuesMethodBuilder.endControlFlow();
        }

        getUpdatingValuesMethodBuilder.addStatement("return null");

        viewToPresenterMapBuilder.addMethod(getUpdatingValuesMethodBuilder.build());


        JavaFile javaFile = JavaFile.builder(KnitFileStrings.KNIT_PACKAGE,
                viewToPresenterMapBuilder.build()).build();


        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
