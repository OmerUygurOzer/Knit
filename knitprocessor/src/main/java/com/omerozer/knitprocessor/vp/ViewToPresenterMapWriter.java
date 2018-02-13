package com.omerozer.knitprocessor.vp;

import com.omerozer.knitprocessor.KnitFileStrings;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;

/**
 * Created by omerozer on 2/6/18.
 */

public class ViewToPresenterMapWriter {
    static void write(Filer filer, Map<KnitPresenterMirror, KnitViewMirror> map) {
        TypeSpec.Builder viewToPresenterMapBuilder = TypeSpec
                .classBuilder(KnitFileStrings.KNIT_VIEW_PRESENTER)
                .addSuperinterface(ClassName.bestGuess(KnitFileStrings.KNIT_VIEW_PRESENTER_INTERFACE))
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder getPresenterMethodBuilder = MethodSpec
                .methodBuilder("getPresenterClass")
                .addParameter(String.class,"view")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Override.class);

        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(ClassName.get(Map.class),
                TypeName.get(String.class),ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER));

        FieldSpec parentMapField = FieldSpec
                .builder(mapTypeName,"parentToPresenterMap")
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec viewMapField = FieldSpec
                .builder(mapTypeName,"viewToPresenterMap")
                .addModifiers(Modifier.PRIVATE)
                .build();

        MethodSpec.Builder constructorBuilder = MethodSpec
                .constructorBuilder()
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL),"modelManager")
                .addStatement("parentToPresenterMap = new $L<>()", HashMap.class.getCanonicalName())
                .addStatement("viewToPresenterMap = new $L<>()", HashMap.class.getCanonicalName());

        constructorBuilder.addStatement("$L presenter",KnitFileStrings.KNIT_PRESENTER);
        for(KnitPresenterMirror knitPresenterMirror : map.keySet()){
                PackageElement element = (PackageElement)map.get(knitPresenterMirror).enclosingClass.getEnclosingElement();
                constructorBuilder.addStatement("presenter = new $L.$L$L(new $L(),modelManager)",element.getQualifiedName(),knitPresenterMirror.enclosingClass.getSimpleName(),KnitFileStrings.KNIT_PRESENTER_POSTFIX,knitPresenterMirror.enclosingClass.getQualifiedName());
                constructorBuilder.addStatement("viewToPresenterMap.put($L.class.getCanonicalName(),$L)",map.get(knitPresenterMirror).enclosingClass.getQualifiedName(),"presenter");
                constructorBuilder.addStatement("parentToPresenterMap.put($L.class.getCanonicalName(),$L)",knitPresenterMirror.enclosingClass.getQualifiedName(),"presenter");
        }
        constructorBuilder.beginControlFlow("for(String key: parentToPresenterMap.keySet())");
        constructorBuilder.addStatement("parentToPresenterMap.get(key).onCreate()");
        constructorBuilder.endControlFlow();

        viewToPresenterMapBuilder.addField(parentMapField);
        viewToPresenterMapBuilder.addField(viewMapField);
        viewToPresenterMapBuilder.addMethod(constructorBuilder.build());

        for(KnitPresenterMirror mirror: map.keySet()){
            PackageElement element = (PackageElement)map.get(mirror).enclosingClass.getEnclosingElement();
            getPresenterMethodBuilder.beginControlFlow("if(view.equals($S))",map.get(mirror).enclosingClass.getQualifiedName().toString());
            getPresenterMethodBuilder.addStatement("return \" $L.$L$L \"",element.getQualifiedName(),mirror.enclosingClass.getSimpleName(),KnitFileStrings.KNIT_PRESENTER_POSTFIX);
            getPresenterMethodBuilder.endControlFlow();
        }



        getPresenterMethodBuilder.addStatement("return \"\" ");
        viewToPresenterMapBuilder.addMethod(getPresenterMethodBuilder.build());

        MethodSpec.Builder createPresenterForViewMethodBuilder = MethodSpec
                .methodBuilder("createPresenterForView")
                .addParameter(Object.class,"viewObject")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL),"modelManager")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER))
                .addAnnotation(Override.class);

        for(KnitPresenterMirror mirror: map.keySet()){
            createPresenterForViewMethodBuilder.beginControlFlow("if(viewObject instanceof $L)",map.get(mirror).enclosingClass.getQualifiedName());
            createPresenterForViewMethodBuilder.addStatement("return viewToPresenterMap.get($L)","viewObject.getClass().getCanonicalName()");
            createPresenterForViewMethodBuilder.endControlFlow();
        }



        MethodSpec.Builder createPresenterForParentMethodBuilder = MethodSpec
                .methodBuilder("getPresenterForParent")
                .addParameter(Object.class,"parentObject")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER))
                .addStatement("return parentToPresenterMap.get($L)","parentObject.getClass().getCanonicalName()")
                .addAnnotation(Override.class);

        viewToPresenterMapBuilder.addMethod(createPresenterForParentMethodBuilder.build());

        createPresenterForViewMethodBuilder.addStatement("return null");

        viewToPresenterMapBuilder.addMethod(createPresenterForViewMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder(KnitFileStrings.KNIT_PACKAGE,viewToPresenterMapBuilder.build()).build();


        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
