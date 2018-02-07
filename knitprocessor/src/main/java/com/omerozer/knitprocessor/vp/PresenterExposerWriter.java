package com.omerozer.knitprocessor.vp;

import com.omerozer.knitprocessor.KnitFileStrings;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;

/**
 * Created by omerozer on 2/4/18.
 */

public class PresenterExposerWriter {
    static void write(Filer filer, KnitPresenterMirror presenterMirror) {

        TypeSpec.Builder clazzBuilder = TypeSpec
                .classBuilder(presenterMirror.enclosingClass.getSimpleName()+ KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX)
                .addModifiers(Modifier.PUBLIC);

        FieldSpec parentField = FieldSpec
                .builder(TypeName.get(presenterMirror.enclosingClass.asType()),"parent", Modifier.PRIVATE).build();

        MethodSpec constructor = MethodSpec
                .constructorBuilder()
                .addParameter(TypeName.get(presenterMirror.enclosingClass.asType()),"parent")
                .addStatement("this.parent = parent")
                .addModifiers(Modifier.PUBLIC)
                .build();

        for(Element element : presenterMirror.enclosingClass.getEnclosedElements()){
            if(element.getKind().isField()){
                MethodSpec getter = MethodSpec
                        .methodBuilder("get_"+element.getSimpleName().toString())
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.get(element.asType()))
                        .addStatement("return parent."+element.getSimpleName())
                        .build();

                clazzBuilder.addMethod(getter);
            }
        }
        clazzBuilder.addField(parentField);
        clazzBuilder.addMethod(constructor);

        PackageElement enclosingPackage =
                (PackageElement) presenterMirror.enclosingClass.getEnclosingElement();

        JavaFile javaFile = JavaFile
                .builder(enclosingPackage.getQualifiedName().toString(), clazzBuilder.build())
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
