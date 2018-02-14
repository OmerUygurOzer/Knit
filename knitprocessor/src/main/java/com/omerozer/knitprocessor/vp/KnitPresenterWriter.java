package com.omerozer.knitprocessor.vp;

import com.omerozer.knit.Use;
import com.omerozer.knit.UseMethod;
import com.omerozer.knitprocessor.KnitFileStrings;
import com.omerozer.knitprocessor.PackageStringExtractor;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 2/2/18.
 */

class KnitPresenterWriter {
    static void write(Filer filer, KnitPresenterMirror presenterMirror,
            Map<KnitPresenterMirror, KnitViewMirror> map) {

        FieldSpec parentField = FieldSpec
                .builder(ClassName.bestGuess(presenterMirror.enclosingClass.getQualifiedName() +
                                KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX), "parent",
                        Modifier.PRIVATE)
                .build();

        FieldSpec loadedField = FieldSpec
                .builder(TypeName.BOOLEAN, "loaded", Modifier.PRIVATE)
                .build();

        FieldSpec modelManagerField = FieldSpec
                .builder(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL), "modelManager",
                        Modifier.PRIVATE)
                .build();

        FieldSpec eventHandlerField = FieldSpec
                .builder(ClassName.bestGuess(KnitFileStrings.KNIT_EVENT_HANDLER), "viewEventHandler",
                        Modifier.PRIVATE)
                .build();

        ClassName contractName = ClassName.bestGuess(presenterMirror.targetView.toString()+KnitFileStrings.KNIT_CONTRACT_POSTFIX);

        FieldSpec activeViewWeakRefField = FieldSpec
                .builder(contractName, "activeViewContract")
                .addModifiers(Modifier.PRIVATE)
                .build();


        MethodSpec shouldLoadMethod;

        MethodSpec.Builder shouldLoadMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_SHOULD_LOAD_METHOD)
                .returns(TypeName.BOOLEAN)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("return !loaded");

        shouldLoadMethod = shouldLoadMethodBuilder.build();

        MethodSpec getModelManagerMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_GET_MODEL_MANAGER_METHOD)
                .returns(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("return this.$L","modelManager")
                .build();

        MethodSpec getContractMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_GET_VIEW_METHOD)
                .returns(Object.class)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("return this.activeViewContract")
                .build();

        MethodSpec onCreateMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_ONCREATE_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("this.parent.use_onCreate()")
                .build();


        TypeSpec.Builder clazzBuilder = TypeSpec
                .classBuilder(presenterMirror.enclosingClass.getSimpleName()
                        + KnitFileStrings.KNIT_PRESENTER_POSTFIX)
                .superclass(ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER))
                .addAnnotation(Use.class)
                .addModifiers(Modifier.PUBLIC)
                .addField(parentField)
                .addField(loadedField)
                .addField(modelManagerField)
                .addField(eventHandlerField)
                .addField(activeViewWeakRefField)
                .addMethod(shouldLoadMethod)
                .addMethod(getModelManagerMethod)
                .addMethod(onCreateMethod)
                .addMethod(getContractMethod);




        createConstructor(clazzBuilder, presenterMirror);
        createApplyMethod(clazzBuilder, presenterMirror, contractName);
        createHandleMethod(clazzBuilder, presenterMirror, map);
        createRemoveMethod(clazzBuilder, presenterMirror);
        createLoadMethod(clazzBuilder, presenterMirror);
        createDestroyMethod(clazzBuilder, presenterMirror);
        createUpdatingMethods(clazzBuilder, presenterMirror, map);


        TypeSpec clazz = clazzBuilder.build();

        String packageName = PackageStringExtractor.extract(presenterMirror.targetView);

        JavaFile javaFile = JavaFile
                .builder(packageName, clazz)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void createConstructor(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {

        MethodSpec.Builder constructorBuilder = MethodSpec
                .constructorBuilder()
                .addParameter(Object.class, "parent")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL), "modelManager")
                .addStatement(
                        "this.parent = new " + presenterMirror.enclosingClass.getQualifiedName()
                                + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX + "(($L)parent)",
                        presenterMirror.enclosingClass.getQualifiedName().toString())
                .addStatement("this.modelManager = modelManager")
                .addStatement("this.viewEventHandler = ($L)parent",KnitFileStrings.KNIT_EVENT_HANDLER)
                .addModifiers(Modifier.PUBLIC);



        constructorBuilder.addStatement("this.loaded = false");

        clazzBuilder.addMethod(constructorBuilder.build());

    }

    private static void createHandleMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror, Map<KnitPresenterMirror, KnitViewMirror> map) {

        MethodSpec.Builder handleMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_EVENT_HANDLE_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_EVENT_VIEW_EVENT_POOL),
                        "eventPool")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_EVENT_VIEW_EVENT_ENV),
                        "eventEnv")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL), "modelManager");

        handleMethodBuilder.addStatement("$L tag = eventEnv.getTag()",
                String.class.getCanonicalName())
                .addStatement("this.viewEventHandler.$L(eventPool,eventEnv,modelManager)",KnitFileStrings.KNIT_EVENT_HANDLE_METHOD);

        clazzBuilder.addMethod(handleMethodBuilder.build());
    }

    private static void createApplyMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror, ClassName name) {

        MethodSpec.Builder applyMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_APPLY_METHOD)
                .addParameter(TypeName.OBJECT, "viewObject")
                .addParameter(ClassName.bestGuess(KnitFileStrings.ANDROID_BUNDLE),"data")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        applyMethodBuilder.addStatement("$L target = ($L)viewObject",
                presenterMirror.targetView.toString(), presenterMirror.targetView.toString());


        applyMethodBuilder.addStatement("this.activeViewContract = new $L(target)",name);
        applyMethodBuilder.addStatement("this.parent.use_onViewApplied(viewObject,data)");

        clazzBuilder.addMethod(applyMethodBuilder.build());
    }

    private static void createRemoveMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {
        MethodSpec.Builder removeActiveView = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_RELEASE_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.activeViewContract = null")
                .addStatement("this.parent.use_onCurrentViewReleased()")
                .addAnnotation(Override.class);

        clazzBuilder.addMethod(removeActiveView.build());
    }

    private static void createLoadMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {
        MethodSpec.Builder loadMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_LOAD_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        loadMethodBuilder.addStatement("this.loaded = true");
        clazzBuilder.addMethod(loadMethodBuilder.build());
    }

    private static void createDestroyMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {

        MethodSpec.Builder destroyMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_DESTROY_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        destroyMethodBuilder.addStatement("this.loaded = false");

        clazzBuilder.addMethod(destroyMethodBuilder.build());
    }


    private static void createUpdatingMethods(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror, Map<KnitPresenterMirror, KnitViewMirror> map) {

        for(String string : presenterMirror.updatingMethodsMap.keySet()){
            MethodSpec.Builder updatingMethodBuilder = MethodSpec
                    .methodBuilder(string+KnitFileStrings.KNIT_PRESENTER_UPDATE_METHOD_POSTFIX)
                    .addModifiers(Modifier.PUBLIC);

            ExecutableElement methodElement = presenterMirror.updatingMethodsMap.get(string);
            int c = 0;
            StringBuilder paramsText = new StringBuilder();
            for(VariableElement param : methodElement.getParameters()){
                paramsText.append("v");
                paramsText.append(Integer.toString(c));
                updatingMethodBuilder.addParameter(TypeName.get(param.asType()),"v"+Integer.toString(c));
                if(c<methodElement.getParameters().size()-1){
                    paramsText.append(",");
                }
                c++;
            }
            updatingMethodBuilder.addStatement("parent.$L$L($L)","use_",methodElement.getSimpleName(),paramsText.toString());
            updatingMethodBuilder.addAnnotation(AnnotationSpec.builder(UseMethod.class)
                            .addMember("value", "$S", string)
                            .build());
            clazzBuilder.addMethod(updatingMethodBuilder.build());
        }

    }




}
