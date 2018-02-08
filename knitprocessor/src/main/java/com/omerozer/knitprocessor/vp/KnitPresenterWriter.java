package com.omerozer.knitprocessor.vp;

import com.omerozer.knit.Getter;
import com.omerozer.knit.Use;
import com.omerozer.knit.UseMethod;
import com.omerozer.knitprocessor.Boxer;
import com.omerozer.knitprocessor.KnitFileStrings;
import com.omerozer.knitprocessor.MutatorTypeNameCreator;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

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

        ParameterizedTypeName activeViewWeakRefName = ParameterizedTypeName.get(
                ClassName.bestGuess(WeakReference.class.getCanonicalName()),
                TypeName.get(presenterMirror.targetView));

        FieldSpec activeViewWeakRefField = FieldSpec
                .builder(activeViewWeakRefName, "activeView")
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


        TypeSpec.Builder clazzBuilder = TypeSpec
                .classBuilder(presenterMirror.enclosingClass.getSimpleName()
                        + KnitFileStrings.KNIT_PRESENTER_POSTFIX)
                .superclass(ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER))
                .addAnnotation(Use.class)
                .addModifiers(Modifier.PUBLIC)
                .addField(parentField)
                .addField(loadedField)
                .addField(modelManagerField)
                .addField(activeViewWeakRefField)
                .addMethod(shouldLoadMethod)
                .addMethod(getModelManagerMethod);

        createConstructor(clazzBuilder, presenterMirror);
        createApplyMethod(clazzBuilder, presenterMirror, map);
        createHandleMethod(clazzBuilder, presenterMirror, map);
        createRemoveMethod(clazzBuilder, presenterMirror);
        createGetters(clazzBuilder, presenterMirror);
        createLoadMethod(clazzBuilder, presenterMirror);
        createDestroyMethod(clazzBuilder, presenterMirror);
        createSeedFields(clazzBuilder, presenterMirror);
        createMutatorFields(clazzBuilder, presenterMirror);
        createHandlerFields(clazzBuilder, presenterMirror);
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
                .addModifiers(Modifier.PUBLIC);

        for (String string : presenterMirror.seedingFields.keySet()) {
            String incFieldName = presenterMirror.seedingFields.get(
                    string).getSimpleName().toString();
            constructorBuilder.addStatement(
                    "this." + string + KnitFileStrings.KNIT_PRESENTER_SEED_FIELD_POSTFIX
                            + " = this.parent.get_" + incFieldName + "()");
        }

        for (String string : presenterMirror.mutatorFields.keySet()) {
            String incFieldName = presenterMirror.mutatorFields.get(
                    string).getSimpleName().toString();
            constructorBuilder.addStatement(
                    "this." + string + KnitFileStrings.KNIT_PRESENTER_MUTATOR_FIELD_POSTFIX
                            + " = this.parent.get_" + incFieldName + "()");
        }

        for (String string : presenterMirror.eventHandlerFields.keySet()) {
            String incFieldName = presenterMirror.eventHandlerFields.get(
                    string).getSimpleName().toString();
            constructorBuilder.addStatement(
                    "this." + string + KnitFileStrings.KNIT_PRESENTER_HANDLER_FIELD_POSTFIX
                            + " = this.parent.get_" + incFieldName + "()");
        }

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
                String.class.getCanonicalName());

        for (String string : presenterMirror.eventHandlerFields.keySet()) {
            handleMethodBuilder.beginControlFlow("if(tag.equals($S))", string);
            handleMethodBuilder.addStatement("this.$L$L.handle($L,$L,$L)", string,
                    KnitFileStrings.KNIT_PRESENTER_HANDLER_FIELD_POSTFIX, "eventPool", "eventEnv",
                    "modelManager");
            handleMethodBuilder.endControlFlow();
        }

        clazzBuilder.addMethod(handleMethodBuilder.build());
    }

    private static void createApplyMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror, Map<KnitPresenterMirror, KnitViewMirror> map) {

        MethodSpec.Builder applyMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_APPLY_METHOD)
                .addParameter(TypeName.OBJECT, "viewObject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        applyMethodBuilder.addStatement("$L target = ($L)viewObject",
                presenterMirror.targetView.toString(), presenterMirror.targetView.toString());


        KnitViewMirror targetView = map.get(presenterMirror);

        for (String string : targetView.leechingFields.keySet()) {
            if (presenterMirror.seedingFields.keySet().contains(string)) {
                applyMethodBuilder.addStatement("target.$L = $L_Seed",
                        targetView.leechingFields.get(string).getSimpleName().toString(),
                        string);

            }
        }
        applyMethodBuilder.addStatement("this.activeView = new WeakReference<>(target)");

        clazzBuilder.addMethod(applyMethodBuilder.build());
    }

    private static void createRemoveMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {
        MethodSpec.Builder removeActiveView = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_RELEASE_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.activeView = null")
                .addAnnotation(Override.class);

        clazzBuilder.addMethod(removeActiveView.build());
    }

    private static void createLoadMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {
        MethodSpec.Builder loadMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_LOAD_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        String allSeeds = KnitFileStrings.createStringArrayField(
                presenterMirror.seedingFields.keySet());

        loadMethodBuilder.addStatement("this.modelManager.request(" + allSeeds + ",this)");
        loadMethodBuilder.addStatement("this.loaded = true");

        clazzBuilder.addMethod(loadMethodBuilder.build());
    }

    private static void createDestroyMethod(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {

        MethodSpec.Builder destroyMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_DESTROY_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        for (String string : presenterMirror.seedingFields.keySet()) {
            destroyMethodBuilder.addStatement(
                    "this." + string + KnitFileStrings.KNIT_PRESENTER_SEED_FIELD_POSTFIX
                            + " = null");
        }
        destroyMethodBuilder.addStatement("this.loaded = false");

        clazzBuilder.addMethod(destroyMethodBuilder.build());
    }

    private static void createSeedFields(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {
        for (String string : presenterMirror.seedingFields.keySet()) {
            FieldSpec seedField = FieldSpec
                    .builder(Boxer.boxIfNeeded(presenterMirror.seedingFields.get(string)),
                            string + KnitFileStrings.KNIT_PRESENTER_SEED_FIELD_POSTFIX)
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            clazzBuilder.addField(seedField);
        }
    }

    private static void createMutatorFields(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {

        for (String string : presenterMirror.mutatorFields.keySet()) {
            ParameterizedTypeName name = MutatorTypeNameCreator.create(string, presenterMirror);

            FieldSpec seedField = FieldSpec
                    .builder(name,
                            string + KnitFileStrings.KNIT_PRESENTER_MUTATOR_FIELD_POSTFIX)
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            clazzBuilder.addField(seedField);
        }
    }

    private static void createHandlerFields(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {

        for (String string : presenterMirror.eventHandlerFields.keySet()) {
            FieldSpec seedField = FieldSpec
                    .builder(ClassName.bestGuess(KnitFileStrings.KNIT_EVENT_HANDLER),
                            string + KnitFileStrings.KNIT_PRESENTER_HANDLER_FIELD_POSTFIX)
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            clazzBuilder.addField(seedField);
        }
    }


    private static void createGetters(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror) {

        for (String string : presenterMirror.seedingFields.keySet()) {
            MethodSpec getterMethod = MethodSpec
                    .methodBuilder("get" + string)
                    .addAnnotation(AnnotationSpec.builder(Getter.class)
                            .addMember("value", "$S", string)
                            .build())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.get(presenterMirror.seedingFields.get(string).asType()))
                    .addStatement("return this." + string + "_Seed")
                    .build();
            clazzBuilder.addMethod(getterMethod);
        }

    }

    private static void createUpdatingMethods(TypeSpec.Builder clazzBuilder,
            KnitPresenterMirror presenterMirror, Map<KnitPresenterMirror, KnitViewMirror> map) {
        for (String string : presenterMirror.seedingFields.keySet()) {
            MethodSpec.Builder seedUpdatingMethodBuilder = MethodSpec
                    .methodBuilder(string + KnitFileStrings.KNIT_PRESENTER_UPDATE_METHOD_POSTFIX)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(AnnotationSpec.builder(UseMethod.class)
                            .addMember("value", "$S", string)
                            .build())
                    .addParameter(Boxer.boxIfNeeded(presenterMirror.seedingFields.get(string)),
                            "val");

            seedUpdatingMethodBuilder.addStatement(
                    "this." + string + KnitFileStrings.KNIT_PRESENTER_SEED_FIELD_POSTFIX
                            + " = val");

            Set<String> mutatedObjects = new HashSet<>();

            for (String reverseMappedVal : reverseMutatorLookup(string, presenterMirror)) {
                if (presenterMirror.mutatorFields.containsKey(reverseMappedVal)) {
                    mutatedObjects.add(reverseMappedVal);
                    String[] params = presenterMirror.mutatorParams.get(reverseMappedVal);
                    StringBuilder paramsBlock = new StringBuilder();
                    paramsBlock.append("(");
                    if (!params[0].equals("")) {
                        for (int i = 0; i < params.length; i++) {
                            paramsBlock.append("this.");
                            paramsBlock.append(params[i]);
                            paramsBlock.append(KnitFileStrings.KNIT_PRESENTER_SEED_FIELD_POSTFIX);
                            if (i < params.length - 1) {
                                paramsBlock.append(",");
                            }
                        }
                    } else {
                        paramsBlock.append("this.");
                        paramsBlock.append(reverseMappedVal);
                        paramsBlock.append(KnitFileStrings.KNIT_PRESENTER_SEED_FIELD_POSTFIX);
                    }
                    paramsBlock.append(")");
                    seedUpdatingMethodBuilder.addStatement(
                            "this." + reverseMappedVal
                                    + KnitFileStrings.KNIT_PRESENTER_SEED_FIELD_POSTFIX +
                                    " = " + reverseMappedVal
                                    + KnitFileStrings.KNIT_PRESENTER_MUTATOR_FIELD_POSTFIX
                                    + ".mutate"
                                    + paramsBlock.toString());
                }
            }

            KnitViewMirror target = map.get(presenterMirror);
            seedUpdatingMethodBuilder.beginControlFlow("if(activeView!=null)");

            for (String updating : target.updatingMethods.keySet()) {
                if (updating.equals(string)) {
                    if (target.leechingFields.containsKey(string)) {
                        seedUpdatingMethodBuilder.addStatement("activeView.get().$L = $L_Seed",
                                target.leechingFields.get(string).getSimpleName(), string);
                    }
                    seedUpdatingMethodBuilder.addStatement("activeView.get().$L($L_Seed)",
                            target.updatingMethods.get(updating).getSimpleName(), string);


                }
            }
            for (String mutated : mutatedObjects) {
                if (target.updatingMethods.containsKey(mutated)) {
                    seedUpdatingMethodBuilder.addStatement("activeView.get().$L($L_Seed)",
                            target.updatingMethods.get(mutated).getSimpleName(),
                            mutated);
                }
            }

            seedUpdatingMethodBuilder.endControlFlow();

            clazzBuilder.addMethod(seedUpdatingMethodBuilder.build());
        }
    }

    private static Set<String> reverseMutatorLookup(String string,
            KnitPresenterMirror presenterMirror) {
        Set<String> keys = new HashSet<>();
        for (String s : presenterMirror.mutatorParams.keySet()) {
            for (String param : presenterMirror.mutatorParams.get(s)) {
                if (param.equals(string)) {
                    keys.add(s);
                }
            }
        }

        return keys;
    }


}
