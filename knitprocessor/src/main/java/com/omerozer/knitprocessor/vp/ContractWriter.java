package com.omerozer.knitprocessor.vp;

import com.omerozer.knitprocessor.KnitFileStrings;
import com.omerozer.knitprocessor.ReturnTypeExaminer;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;

/**
 * Created by omerozer on 2/14/18.
 */

public class
ContractWriter {

    static void write(Filer filer, KnitViewMirror viewMirror) {

        TypeSpec.Builder contractClassBuilder = TypeSpec
                .classBuilder(viewMirror.enclosingClass.getSimpleName()+ KnitFileStrings.KNIT_CONTRACT_POSTFIX)
                .addModifiers(Modifier.PUBLIC);

        ParameterizedTypeName parentWeakName = ParameterizedTypeName.get(
                ClassName.bestGuess(WeakReference.class.getCanonicalName()),
                TypeName.get(viewMirror.enclosingClass.asType()));

        FieldSpec parentWeakField = FieldSpec
                .builder(parentWeakName,"parent")
                .addModifiers(Modifier.PRIVATE)
                .build();

        MethodSpec constructor = MethodSpec
                .constructorBuilder()
                .addParameter(TypeName.get(viewMirror.enclosingClass.asType()),"parent")
                .addStatement("this.parent = new WeakReference<>(parent)")
                .build();

        MethodSpec nullCheckMethod = MethodSpec
                .methodBuilder("nullCheck")
                .returns(TypeName.BOOLEAN)
                .addStatement("return this.parent==null || this.parent.get()==null")
                .build();

        contractClassBuilder.addMethod(nullCheckMethod);

        for(ExecutableElement executableElement : viewMirror.methods){
            MethodSpec.Builder methodBuilder = MethodSpec
                    .methodBuilder(executableElement.getSimpleName().toString())
                    .addModifiers(Modifier.PUBLIC);

            int c = 0;
            StringBuilder paramBlock = new StringBuilder();
            for(Element param : executableElement.getParameters()){
                paramBlock.append(param.getSimpleName());
                methodBuilder.addParameter(TypeName.get(param.asType()),param.getSimpleName().toString());
                if(c < executableElement.getParameters().size()-1){paramBlock.append(",");}
                c++;
            }

            if(executableElement.getReturnType().toString().contains("void")){
                methodBuilder.beginControlFlow("if(nullCheck())");
                methodBuilder.addStatement("return");
                methodBuilder.endControlFlow();
                methodBuilder.addStatement("this.parent.get().$L($L)",executableElement.getSimpleName(),paramBlock);
            }else{
                TypeName returnType = TypeName.get(executableElement.getReturnType());
                methodBuilder.returns(returnType);

                methodBuilder.beginControlFlow("if(nullCheck())");
                methodBuilder.addStatement("return $L", ReturnTypeExaminer.getDefaultReturnValueInString(returnType));
                methodBuilder.endControlFlow();

                methodBuilder.addStatement("return this.parent.get().$L($L)",executableElement.getSimpleName(),paramBlock);
            }

            contractClassBuilder.addMethod(methodBuilder.build());

        }

        contractClassBuilder.addMethod(constructor);
        contractClassBuilder.addField(parentWeakField);

        PackageElement packageElement = (PackageElement)viewMirror.enclosingClass.getEnclosingElement();

        JavaFile javaFile = JavaFile.builder(packageElement.getQualifiedName().toString(),contractClassBuilder.build()).build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }







}
