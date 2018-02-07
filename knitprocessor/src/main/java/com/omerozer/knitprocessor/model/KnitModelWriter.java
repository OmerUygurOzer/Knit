package com.omerozer.knitprocessor.model;

import com.omerozer.knit.Generates;
import com.omerozer.knit.GeneratesAsync;
import com.omerozer.knit.UseMethod;
import com.omerozer.knitprocessor.KnitFileStrings;
import com.omerozer.knitprocessor.user.UserMirror;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by omerozer on 2/4/18.
 */

class KnitModelWriter {
    static void write(Filer filer, KnitModelMirror modelMirror,
            Map<KnitModelMirror, Set<UserMirror>> map) {

        TypeSpec.Builder clazzBuilder = TypeSpec
                .classBuilder(modelMirror.enclosingClass.getSimpleName()
                        + KnitFileStrings.KNIT_MODEL_POSTFIX)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL));

        Set<String> handledVals = new HashSet<>();

        for (String[] params : modelMirror.generateField.keySet()) {
            handledVals.addAll(Arrays.asList(params));
        }

        for (String[] params : modelMirror.generateAsyncField.keySet()) {
            handledVals.addAll(Arrays.asList(params));
        }

        String handledValsArray = KnitFileStrings.createStringArrayField(handledVals);

        MethodSpec getHandledValsMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_MODEL_GETHANDLEDVALUES)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(String[].class)
                .addStatement("return " + handledValsArray)
                .build();

        FieldSpec parentExposerField = FieldSpec
                .builder(ClassName.bestGuess(
                        modelMirror.enclosingClass.getQualifiedName().toString()
                                + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX), "parentExposer")
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec asyncHandlerField = FieldSpec
                .builder(ClassName.bestGuess(KnitFileStrings.KNIT_ASYNC_TASK), "asyncHandler")
                .addModifiers(Modifier.PRIVATE,Modifier.FINAL)
                .build();

        FieldSpec uiThreadHandlerField = FieldSpec
                .builder(ClassName.bestGuess(KnitFileStrings.ANDROID_HANDLER), "uiThreadHandler")
                .addModifiers(Modifier.PRIVATE,Modifier.FINAL)
                .build();

        FieldSpec instanceMapField = FieldSpec
                .builder(ParameterizedTypeName.get(ClassName.get(Map.class),
                        ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER),
                        ClassName.bestGuess(KnitFileStrings.KNIT_USER)), "userMap")
                .addModifiers(Modifier.PRIVATE)
                .build();

        clazzBuilder.addField(parentExposerField);
        clazzBuilder.addField(asyncHandlerField);
        clazzBuilder.addField(uiThreadHandlerField);
        clazzBuilder.addField(instanceMapField);

        createRequestMethod(clazzBuilder, modelMirror, map);
        createGeneratingFields(clazzBuilder, modelMirror);
        createConstructor(clazzBuilder, modelMirror);

        clazzBuilder.addMethod(getHandledValsMethod);


        PackageElement packageElement =
                (PackageElement) modelMirror.enclosingClass.getEnclosingElement();


        JavaFile javaFile = JavaFile
                .builder(packageElement.getQualifiedName().toString(), clazzBuilder.build())
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void createGeneratingFields(TypeSpec.Builder clazzBuilder,
            KnitModelMirror modelMirror) {
        for (VariableElement generator : modelMirror.generateAsyncField.values()) {
            FieldSpec generatorField = FieldSpec
                    .builder(TypeName.get(generator.asType()), generator.getSimpleName().toString(),
                            Modifier.PRIVATE)
                    .build();
            clazzBuilder.addField(generatorField);
        }

        for (VariableElement generator : modelMirror.generateField.values()) {
            FieldSpec generatorField = FieldSpec
                    .builder(TypeName.get(generator.asType()), generator.getSimpleName().toString(),
                            Modifier.PRIVATE)
                    .build();
            clazzBuilder.addField(generatorField);
        }
    }

    private static void createConstructor(TypeSpec.Builder clazzBuilder,
            KnitModelMirror modelMirror) {

        MethodSpec.Builder constructorBuilder = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Object.class, "parent")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_ASYNC_TASK), "asyncHandler")
                .addStatement("this.parentExposer = new "
                        + modelMirror.enclosingClass.getQualifiedName().toString()
                        + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX + "(($L)parent)",modelMirror.enclosingClass.getQualifiedName().toString())
                .addStatement("this.asyncHandler = asyncHandler")
                .addStatement("this.uiThreadHandler = new " + KnitFileStrings.ANDROID_HANDLER + "("
                        + KnitFileStrings.ANDROID_LOOPER + ".getMainLooper())")
                .addStatement("this.userMap = new java.util.HashMap<>()");

        for (VariableElement generator : modelMirror.generateField.values()) {
            constructorBuilder
                    .addStatement("this." + generator.getSimpleName().toString()
                            + " = this.parentExposer.get_" + generator.getSimpleName() + "()");

        }

        for (VariableElement generator : modelMirror.generateAsyncField.values()) {
            constructorBuilder
                    .addStatement("this." + generator.getSimpleName().toString()
                            + " = this.parentExposer.get_" + generator.getSimpleName() + "()");

        }


        clazzBuilder.addMethod(constructorBuilder.build());
    }

    private static void createRequestMethod(TypeSpec.Builder clazzBuilder,
            KnitModelMirror modelMirror, Map<KnitModelMirror, Set<UserMirror>> map) {
        MethodSpec.Builder requestMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_MODEL_REQUEST_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String[].class, "params")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER), "presenter");

        requestMethodBuilder
                .beginControlFlow("for(String requestedVal: params)");

        createGenerateMethodInvokers(modelMirror, requestMethodBuilder, modelMirror.generateField,
                map, Generates.class);
        createGenerateMethodInvokers(modelMirror, requestMethodBuilder,
                modelMirror.generateAsyncField, map, GeneratesAsync.class);


        requestMethodBuilder.endControlFlow();
        clazzBuilder.addMethod(requestMethodBuilder.build());
    }

    private static void createGenerateMethodInvokers(KnitModelMirror modelMirror,
            MethodSpec.Builder requestMethodBuilder, Map<String[], VariableElement> env,
            Map<KnitModelMirror, Set<UserMirror>> map, Class<?> clazz) {
        for (String[] params : env.keySet()) {
            requestMethodBuilder.beginControlFlow("if(" + createConditionBlock(params) + ")");
            if (map.containsKey(modelMirror)) {
                for (UserMirror userMirror : map.get(modelMirror)) {
                    requestMethodBuilder.beginControlFlow("if(presenter instanceof "
                            + userMirror.enclosingClass.getQualifiedName() + ")");
                    requestMethodBuilder.beginControlFlow("if(!userMap.containsKey(presenter))");
                    requestMethodBuilder.addStatement(
                            "this.userMap.put(presenter,new " + createUserName(
                                    userMirror.enclosingClass) + "(presenter))");
                    requestMethodBuilder.endControlFlow();

                    if (findMethod(params, userMirror)) {
                        requestMethodBuilder.addStatement("final "+
                                createUserName(userMirror.enclosingClass) + " cur = "
                                        + createCastUserName(userMirror.enclosingClass));
                        if (clazz.equals(GeneratesAsync.class)) {
                            requestMethodBuilder.addStatement("asyncHandler.submitTask($L)",createAsyncRunnable(params,modelMirror,userMirror));


                        } else if (clazz.equals(Generates.class)) {
                            requestMethodBuilder.addStatement(
                                    "cur" + findAndCreateMethod(params, userMirror)
                                            + createGenerateParams(params, modelMirror, userMirror,
                                            clazz) + ")");
                        }


                    }


                    requestMethodBuilder.endControlFlow();
                }

            }
            requestMethodBuilder.endControlFlow();
        }
    }

    private static String createConditionBlock(String[] params) {
        StringBuilder conditionBuilder = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            conditionBuilder.append("\"");
            conditionBuilder.append(params[i]);
            conditionBuilder.append("\"");
            conditionBuilder.append(".equals(requestedVal)");

            if (i < params.length - 1) {
                conditionBuilder.append("||");
            }
        }
        return conditionBuilder.toString();
    }

    private static String createUserName(TypeElement typeElement) {
        return typeElement.getQualifiedName() + KnitFileStrings.KNIT_PRESENTER_USER_POSTFIX;
    }

    private static String createCastUserName(TypeElement typeElement) {
        return "(" + createUserName(typeElement) + ")userMap.get(presenter)";
    }

    private static boolean findMethod(String[] params, UserMirror userMirror) {
        for (String param : params) {
            for (ExecutableElement executableElement : userMirror.method) {
                if (executableElement.getAnnotation(UseMethod.class).value().equals(param)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String findAndCreateMethod(String[] params, UserMirror userMirror) {
        for (String param : params) {
            for (ExecutableElement executableElement : userMirror.method) {
                if (executableElement.getAnnotation(UseMethod.class).value().equals(param)) {
                    return ".use_" + executableElement.getSimpleName() + "(";
                }
            }
        }
        return "";
    }

    private static String findMethodAndCastObjectToItsParam(String[] params, UserMirror userMirror) {
        for (String param : params) {
            for (ExecutableElement executableElement : userMirror.method) {
                if (executableElement.getAnnotation(UseMethod.class).value().equals(param)) {
                    if(!executableElement.getParameters().isEmpty()){
                        return ".use_"+executableElement.getSimpleName()+"(("+executableElement.getParameters().get(0).asType().toString()+")";
                    }
                    return ".use_" + executableElement.getSimpleName() + "(";
                }
            }
        }
        return "";
    }

    private static String createGenerateParams(String[] params, KnitModelMirror knitModelMirror,
            UserMirror userMirror, Class<?> annotation) {
        String[] takes = null;

        if (annotation.equals(Generates.class)) {
            takes = knitModelMirror.generateField.get(params).getAnnotation(
                    Generates.class).takes();
        } else if (annotation.equals(GeneratesAsync.class)) {
            takes = knitModelMirror.generateAsyncField.get(params).getAnnotation(
                    GeneratesAsync.class).takes();
        }

        if (takes[0].equals("") && annotation.equals(Generates.class)) {
            return knitModelMirror.generateField.get(params).getSimpleName() + ".generate()";
        }

        if (takes[0].equals("") && annotation.equals(GeneratesAsync.class)) {
            return knitModelMirror.generateAsyncField.get(params).getSimpleName() + ".generate()";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (int i = 0; i < takes.length; i++) {
            stringBuilder.append("cur.");
            stringBuilder.append(userMirror.getterMap.get(takes[i]).getSimpleName());
            stringBuilder.append("()");
            if (i < takes.length - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(")");
        return  stringBuilder.toString();
    }

    private static String findVarTypeAndInitialize(String[] params, KnitModelMirror knitModelMirror,
            UserMirror userMirror) {

        String[] takes = knitModelMirror.generateAsyncField.get(params).getAnnotation(
                GeneratesAsync.class).takes();

        String beg = "final java.lang.Object gen = ";

        if (takes[0].equals("")) {
            return beg + knitModelMirror.generateAsyncField.get(params).getSimpleName() + ".generate()";
        }

        return  beg + knitModelMirror.generateAsyncField.get(params).getSimpleName()+".generate" + createGenerateParams(params,
                knitModelMirror, userMirror, GeneratesAsync.class);
    }

    private static TypeSpec createAsyncRunnable(String[] params, KnitModelMirror modelMirror,
            UserMirror userMirror){

        TypeSpec uiRunnable = TypeSpec
                .anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(MethodSpec
                        .methodBuilder("run")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("cur" + findMethodAndCastObjectToItsParam(params, userMirror) +  "gen)")
                        .build())
                .build();


        TypeSpec asyncRunnable = TypeSpec
                .anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(MethodSpec
                        .methodBuilder("run")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement(findVarTypeAndInitialize(params,modelMirror,userMirror))
                        .addStatement("uiThreadHandler.post($L)",uiRunnable)
                        .build())
                .build();




        return asyncRunnable;
    }
}
