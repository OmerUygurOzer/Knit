package com.omerozer.knitprocessor.model;

import com.omerozer.knit.Generates;
import com.omerozer.knit.GeneratesAsync;
import com.omerozer.knit.UseMethod;
import com.omerozer.knitprocessor.GeneratorExaminer;
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
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

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

        MethodSpec onCreateMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_PRESENTER_ONCREATE_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.parentExposer.getParent().onCreate()")
                .build();

        FieldSpec parentExposerField = FieldSpec
                .builder(ClassName.bestGuess(
                        modelMirror.enclosingClass.getQualifiedName().toString()
                                + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX), "parentExposer")
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec asyncHandlerField = FieldSpec
                .builder(ClassName.bestGuess(KnitFileStrings.KNIT_ASYNC_TASK), "asyncHandler")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();

        FieldSpec uiThreadHandlerField = FieldSpec
                .builder(ClassName.bestGuess(KnitFileStrings.ANDROID_HANDLER), "uiThreadHandler")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
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
        clazzBuilder.addMethod(onCreateMethod);
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
                                + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX + "(($L)parent)",
                        modelMirror.enclosingClass.getQualifiedName().toString())
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
                .addParameter(String.class, "data")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER), "presenter")
                .addParameter(Object[].class, "params",Modifier.FINAL);


        for (String[] generatedVals : modelMirror.generateField.keySet()) {
            String condition = createConditionBlock(generatedVals);
            requestMethodBuilder.beginControlFlow("if($L)", condition);

            Set<UserMirror> users = map.get(modelMirror);

            for (UserMirror userMirror : users) {
                if (methodExists(generatedVals, userMirror)) {
                    requestMethodBuilder.beginControlFlow("if(presenter instanceof $L)",
                            userMirror.enclosingClass.getQualifiedName());
                    requestMethodBuilder.beginControlFlow("if(!userMap.containsKey($L))",
                            "presenter");
                    requestMethodBuilder.addStatement("userMap.put(presenter,new $L$L($L))",
                            userMirror.enclosingClass.getQualifiedName(),
                            KnitFileStrings.KNIT_PRESENTER_USER_POSTFIX, "presenter");
                    requestMethodBuilder.endControlFlow();
                    String userText = userMirror.enclosingClass.getQualifiedName() +
                            KnitFileStrings.KNIT_PRESENTER_USER_POSTFIX;
                    requestMethodBuilder.addStatement("$L user = ($L)userMap.get(presenter)",
                            userText,
                            userText);

                    List<String> types = GeneratorExaminer.getGenerateTypes(
                            modelMirror.generateField.get(generatedVals));


                    requestMethodBuilder.addStatement("final $L genVal = $L.generate($L)",
                            types.get(0),
                            modelMirror.generateField.get(generatedVals).getSimpleName(),
                            createParamBlock(types));
                    requestMethodBuilder.addStatement("user.$L(genVal)",
                            findMethod(generatedVals, userMirror));

                    requestMethodBuilder.endControlFlow();
                }
            }


            requestMethodBuilder.endControlFlow();
        }

        for (String[] generatedVals : modelMirror.generateAsyncField.keySet()) {
            String condition = createConditionBlock(generatedVals);
            requestMethodBuilder.beginControlFlow("if($L)", condition);

            Set<UserMirror> users = map.get(modelMirror);

            for (UserMirror userMirror : users) {
                if (methodExists(generatedVals, userMirror)) {
                    requestMethodBuilder.beginControlFlow("if(presenter instanceof $L)",
                            userMirror.enclosingClass.getQualifiedName());
                    requestMethodBuilder.beginControlFlow("if(!userMap.containsKey($L))",
                            "presenter");
                    requestMethodBuilder.addStatement("userMap.put(presenter,new $L$L($L))",
                            userMirror.enclosingClass.getQualifiedName(),
                            KnitFileStrings.KNIT_PRESENTER_USER_POSTFIX, "presenter");
                    requestMethodBuilder.endControlFlow();
                    String userText = userMirror.enclosingClass.getQualifiedName() +
                            KnitFileStrings.KNIT_PRESENTER_USER_POSTFIX;
                    requestMethodBuilder.addStatement("final $L user = ($L)userMap.get(presenter)",
                            userText,
                            userText);

                    requestMethodBuilder.addStatement("asyncHandler.submitTask($L)",createAsyncRunnable(generatedVals,modelMirror,userMirror));

                    requestMethodBuilder.endControlFlow();
                }
            }


            requestMethodBuilder.endControlFlow();
        }


        clazzBuilder.addMethod(requestMethodBuilder.build());
    }


    private static String createParamBlock(List<String> params) {
        StringBuilder paramBuilder = new StringBuilder();

        for (int i = 1; i < params.size(); i++) {
            paramBuilder.append("(");
            paramBuilder.append(params.get(i));
            paramBuilder.append(")");
            paramBuilder.append("params[");
            paramBuilder.append(i - 1);
            paramBuilder.append("]");

            if (i < params.size() - 1) {
                paramBuilder.append(",");
            }
        }
        return paramBuilder.toString();
    }


    private static String createConditionBlock(String[] params) {
        StringBuilder conditionBuilder = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            conditionBuilder.append("\"");
            conditionBuilder.append(params[i]);
            conditionBuilder.append("\"");
            conditionBuilder.append(".equals(data)");

            if (i < params.length - 1) {
                conditionBuilder.append("||");
            }
        }
        return conditionBuilder.toString();
    }

    private static String createUserName(TypeElement typeElement) {
        return typeElement.getQualifiedName() + KnitFileStrings.KNIT_PRESENTER_USER_POSTFIX;
    }

    private static String findMethod(String[] params, UserMirror userMirror) {
        for (String param : params) {
            for (ExecutableElement executableElement : userMirror.method) {
                if (executableElement.getAnnotation(UseMethod.class).value().equals(param)) {
                    return "use_" + executableElement.getSimpleName();
                }
            }
        }
        return "";
    }

    private static boolean methodExists(String[] params, UserMirror userMirror) {
        for (String param : params) {
            for (ExecutableElement executableElement : userMirror.method) {
                if (executableElement.getAnnotation(UseMethod.class).value().equals(param)) {
                    return true;
                }
            }
        }
        return false;
    }


    private static String findMethodAndCastObjectToItsParam(String[] params,
            UserMirror userMirror) {
        for (String param : params) {
            for (ExecutableElement executableElement : userMirror.method) {
                if (executableElement.getAnnotation(UseMethod.class).value().equals(param)) {
                    if (!executableElement.getParameters().isEmpty()) {
                        return ".use_" + executableElement.getSimpleName() + "(("
                                + executableElement.getParameters().get(0).asType().toString()
                                + ")";
                    }
                    return ".use_" + executableElement.getSimpleName() + "(";
                }
            }
        }
        return "";
    }


    private static TypeSpec createAsyncRunnable(String[] params, KnitModelMirror modelMirror,
            UserMirror userMirror) {

        List<String> types = GeneratorExaminer.getGenerateTypes(
                modelMirror.generateAsyncField.get(params));

        TypeSpec uiRunnable = TypeSpec
                .anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(MethodSpec
                        .methodBuilder("run")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("user.$L(genVal)",
                                findMethod(params, userMirror))
                        .build())
                .build();


        return TypeSpec
                .anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(MethodSpec
                        .methodBuilder("run")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("final $L genVal = $L.generate($L)",
                                types.get(0),
                                modelMirror.generateAsyncField.get(params).getSimpleName(),
                                createParamBlock(types))
                        .addStatement("uiThreadHandler.post($L)", uiRunnable)
                        .build())
                .build();

    }
}
