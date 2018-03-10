package com.omerozer.knitprocessor.vp;

import com.omerozer.knitprocessor.InterfaceMethodsCreatorForExposers;
import com.omerozer.knitprocessor.KnitFileStrings;
import com.omerozer.knitprocessor.KnitMethodsFilter;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.VariableElement;

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

        MethodSpec getParentMethod = MethodSpec
                .methodBuilder("getParent")
                .returns(TypeName.get(presenterMirror.enclosingClass.asType()))
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.parent")
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
            }else if(element.getKind().equals(ElementKind.METHOD) && KnitMethodsFilter.filter(element)){
                ExecutableElement methodElement = (ExecutableElement)element;
                MethodSpec.Builder userMethodBuilder = MethodSpec
                        .methodBuilder("use_"+element.getSimpleName().toString())
                        .addModifiers(Modifier.PUBLIC);

                int c = 0;
                StringBuilder paramsText = new StringBuilder();
                for(VariableElement param : methodElement.getParameters()){
                    paramsText.append("v");
                    paramsText.append(Integer.toString(c));
                    userMethodBuilder.addParameter(TypeName.get(param.asType()),"v"+Integer.toString(c));
                    if(c<methodElement.getParameters().size()-1){
                        paramsText.append(",");
                    }
                    c++;
                }
                userMethodBuilder.addStatement("parent.$L($L)",methodElement.getSimpleName(),paramsText.toString());
                clazzBuilder.addMethod(userMethodBuilder.build());
            }
        }

        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnCreateMethod());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnViewAppliedMethod());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnCurrentViewReleased());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnLoadMethod());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnMemoryLow());
        clazzBuilder.addMethod(InterfaceMethodsCreatorForExposers.getOnDestroyMethod());

        for(String callback: NativeViewCallbacks.getAll()){
            clazzBuilder.addMethod(MethodSpec
                    .methodBuilder("use_"+callback)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("this.parent.$L()",callback)
                    .build());
        }


        clazzBuilder.addField(parentField);
        clazzBuilder.addMethod(constructor);
        clazzBuilder.addMethod(getParentMethod);

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
