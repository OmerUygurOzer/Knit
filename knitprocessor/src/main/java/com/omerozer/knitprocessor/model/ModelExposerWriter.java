package com.omerozer.knitprocessor.model;

import com.omerozer.knitprocessor.KnitFileStrings;
import com.omerozer.knitprocessor.model.KnitModelMirror;
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

class ModelExposerWriter {
    static void write(Filer filer, KnitModelMirror modelMirror) {

        TypeSpec.Builder clazzBuilder = TypeSpec
                .classBuilder(modelMirror.enclosingClass.getSimpleName()+ KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX);

        FieldSpec parentField = FieldSpec
                .builder(TypeName.get(modelMirror.enclosingClass.asType()),"parent", Modifier.PRIVATE).build();

        MethodSpec constructor = MethodSpec
                .constructorBuilder()
                .addParameter(TypeName.get(modelMirror.enclosingClass.asType()),"parent")
                .addStatement("this.parent = parent")
                .build();

        for(Element element : modelMirror.enclosingClass.getEnclosedElements()){
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
                (PackageElement) modelMirror.enclosingClass.getEnclosingElement();

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
