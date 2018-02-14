package com.omerozer.knitprocessor.user;

import com.omerozer.knitprocessor.KnitFileStrings;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 2/5/18.
 */

public class UserWriter {
    static void write(Filer filer, UserMirror userMirror) {

        TypeSpec.Builder clazzBuilder = TypeSpec
                .classBuilder(
                        userMirror.enclosingClass.getSimpleName() + KnitFileStrings.KNIT_PRESENTER_USER_POSTFIX)
                .addSuperinterface(ClassName.bestGuess(KnitFileStrings.KNIT_USER))
                .addModifiers(Modifier.PUBLIC);

        FieldSpec parentField = FieldSpec
                .builder(TypeName.get(userMirror.enclosingClass.asType()), "parent")
                .addModifiers(Modifier.PRIVATE)
                .build();

        MethodSpec constructor = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER), "parent")
                .addStatement("this.parent =("+ userMirror.enclosingClass.getQualifiedName()  + ")parent")
                .build();

        for (ExecutableElement methodElement : userMirror.method) {
            MethodSpec.Builder userBuilder = MethodSpec
                    .methodBuilder("use_" + methodElement.getSimpleName().toString())
                    .addModifiers(Modifier.PUBLIC);
            int c = 0;
            for (VariableElement variableElement : methodElement.getParameters()) {
                userBuilder.addParameter(TypeName.get(variableElement.asType()), "v" + c++);
            }
            StringBuilder paramsBlock = new StringBuilder();
            paramsBlock.append("parent.");
            paramsBlock.append(methodElement.getSimpleName());
            paramsBlock.append("(");
            for (int i = 0; i < c; i++) {
                paramsBlock.append("v");
                paramsBlock.append(i);
                if (i < c - 1) {
                    paramsBlock.append(",");
                }
            }
            paramsBlock.append(")");
            userBuilder.addStatement(paramsBlock.toString());
            clazzBuilder.addMethod(userBuilder.build());
        }

        for (String string : userMirror.getterMap.keySet()) {
                MethodSpec getterMethod = MethodSpec
                        .methodBuilder("get"+string)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.get(userMirror.getterMap.get(string).getReturnType()))
                        .addStatement("return this.parent.get"+string+"()")
                        .build();
                clazzBuilder.addMethod(getterMethod);
        }


        clazzBuilder.addMethod(constructor);
        clazzBuilder.addField(parentField);

        PackageElement packageElement = (PackageElement)userMirror.enclosingClass.getEnclosingElement() ;

        JavaFile javaFile = JavaFile
                .builder(packageElement.getQualifiedName().toString(),clazzBuilder.build())
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
