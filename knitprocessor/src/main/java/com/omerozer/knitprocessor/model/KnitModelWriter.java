package com.omerozer.knitprocessor.model;

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
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
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
                .superclass(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL));

        Set<String> handledVals = new HashSet<>();

        for (String[] params : modelMirror.generatesParamsMap.keySet()) {
            handledVals.addAll(Arrays.asList(params));
        }

        for (String[] params : modelMirror.collectorField.keySet()) {
            handledVals.addAll(Arrays.asList(params));
        }

        for (String[] params : modelMirror.inputterField.keySet()) {
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
                .methodBuilder(KnitFileStrings.KNIT_ME_ONCREATE_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.parentExposer.use_$L()",KnitFileStrings.KNIT_ME_ONCREATE_METHOD)
                .build();

        MethodSpec onDestroyMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_ME_DESTROY_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.parentExposer.use_$L()",KnitFileStrings.KNIT_ME_DESTROY_METHOD)
                .build();

        MethodSpec onLoadMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_ME_LOAD_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.parentExposer.use_$L()",KnitFileStrings.KNIT_ME_LOAD_METHOD)
                .build();

        MethodSpec onMemoryLow = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_ME_MEMORY_LOW_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.parentExposer.use_$L()",KnitFileStrings.KNIT_ME_MEMORY_LOW_METHOD)
                .build();

        MethodSpec getParentMethod = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_MODEL_GET_PARENT_METHOD)
                .returns(ClassName.bestGuess(KnitFileStrings.KNIT_MODEL_EXT))
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.parentExposer.getParent()")
                .build();

        FieldSpec parentExposerField = FieldSpec
                .builder(ClassName.bestGuess(
                        modelMirror.enclosingClass.getQualifiedName().toString()
                                + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX), "parentExposer")
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec schedulerProviderField = FieldSpec
                .builder(KnitFileStrings.TYPE_NAME_SCHEDULER_PROVIDER, "schedulerProvider")
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
        clazzBuilder.addField(schedulerProviderField);
        clazzBuilder.addField(uiThreadHandlerField);
        clazzBuilder.addField(instanceMapField);
        clazzBuilder.addMethod(onCreateMethod);
        createRequestMethodForPresenter(clazzBuilder, modelMirror, map);
        createRequestMethodForCallback(clazzBuilder, modelMirror);
        createGeneratingFields(clazzBuilder, modelMirror);
        createConstructor(clazzBuilder, modelMirror);
        createInputMethod(clazzBuilder, modelMirror);

        clazzBuilder.addMethod(getHandledValsMethod);
        clazzBuilder.addMethod(onDestroyMethod);
        clazzBuilder.addMethod(onLoadMethod);
        clazzBuilder.addMethod(onMemoryLow);
        clazzBuilder.addMethod(getParentMethod);


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

        Set<VariableElement> fields = new HashSet<>();
        for(GeneratesParams generatesParams : modelMirror.generatesParamsMap.values()){fields.add(generatesParams.variableElement);}
        fields.addAll(modelMirror.collectorField.values());
        fields.addAll(modelMirror.inputterField.values());

        for (VariableElement field : fields) {
            FieldSpec generatorField = FieldSpec
                    .builder(TypeName.get(field.asType()), field.getSimpleName().toString(),
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
                .addStatement("java.lang.Object parent = new $L()",modelMirror.enclosingClass.getQualifiedName())
                .addParameter(KnitFileStrings.TYPE_NAME_SCHEDULER_PROVIDER, "schedulerProvider")
                .addStatement("this.parentExposer = new "
                                + modelMirror.enclosingClass.getQualifiedName().toString()
                                + KnitFileStrings.KNIT_MODEL_EXPOSER_POSTFIX + "(($L)parent)",
                        modelMirror.enclosingClass.getQualifiedName().toString())
                .addStatement("this.schedulerProvider = schedulerProvider")
                .addStatement("this.uiThreadHandler = new " + KnitFileStrings.ANDROID_HANDLER + "("
                        + KnitFileStrings.ANDROID_LOOPER + ".getMainLooper())")
                .addStatement("this.userMap = new java.util.HashMap<>()");

        Set<VariableElement> fields = new HashSet<>();
        for(GeneratesParams generatesParams : modelMirror.generatesParamsMap.values()){fields.add(generatesParams.variableElement);}
        fields.addAll(modelMirror.collectorField.values());
        fields.addAll(modelMirror.inputterField.values());

        for (VariableElement generator : fields) {
            constructorBuilder
                    .addStatement("this." + generator.getSimpleName().toString()
                            + " = this.parentExposer.get_" + generator.getSimpleName() + "()");

        }

        clazzBuilder.addMethod(constructorBuilder.build());
    }

    private static void createInputMethod(TypeSpec.Builder clazzBuilder,
            KnitModelMirror modelMirror) {

        MethodSpec.Builder inputMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_MODEL_INPUT_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "data")
                .addParameter(Object[].class, "params", Modifier.FINAL);

        for (String[] generatedVals : modelMirror.inputterField.keySet()) {
            List<String> types = GeneratorExaminer.getGenerateTypes(
                    modelMirror.inputterField.get(generatedVals));
            String condition = createConditionBlock(generatedVals);
            inputMethodBuilder.beginControlFlow("if($L)", condition);
            inputMethodBuilder.addStatement("$L.input($L)",
                    modelMirror.inputterField.get(generatedVals).getSimpleName(),
                    createParamBlock(types, 0));
            inputMethodBuilder.endControlFlow();
        }

        clazzBuilder.addMethod(inputMethodBuilder.build());
    }

    private static void createRequestMethodForPresenter(TypeSpec.Builder clazzBuilder,
            KnitModelMirror modelMirror, Map<KnitModelMirror, Set<UserMirror>> map) {

        MethodSpec.Builder requestMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_MODEL_REQUEST_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "data")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_PRESENTER), "presenter")
                .addParameter(Object[].class, "params", Modifier.FINAL);


        for (String[] generatedVals : modelMirror.generatesParamsMap.keySet()) {
            if (map.containsKey(modelMirror)) {
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

                        List<String> types = GeneratorExaminer.getGenerateTypes(
                                modelMirror.generatesParamsMap.get(generatedVals).variableElement);

                        String runOnString = "schedulerProvider."+modelMirror.generatesParamsMap.get(generatedVals).runOn.getMethod();
                        String consumeOnString = "schedulerProvider."+modelMirror.generatesParamsMap.get(generatedVals).consumeOn.getMethod();
                        String knitTaskFlow = KnitFileStrings.KNIT_TASK_FLOW;

                        TypeSpec callable = createCallableTypeSpec(generatedVals,types,modelMirror);
                        TypeSpec consumer = createConsumerTypeSpec(generatedVals,types,modelMirror,userMirror);

                        requestMethodBuilder.addStatement("$L.create($L).runOn($L).consumeOn($L).start($L)",knitTaskFlow,callable,runOnString,consumeOnString,consumer);

                        requestMethodBuilder.endControlFlow();
                    }
                }
                requestMethodBuilder.endControlFlow();
            }

        }

        for (String[] generatedVals : modelMirror.collectorField.keySet()) {
            if (map.containsKey(modelMirror)) {
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
                        requestMethodBuilder.addStatement(
                                "final $L user = ($L)userMap.get(presenter)",
                                userText,
                                userText);

                        List<String> types = GeneratorExaminer.getGenerateTypes(
                                modelMirror.collectorField.get(generatedVals));

                        String paramBlock = createParamBlock(types, 1);

                        if (paramBlock.equals("")) {
                            requestMethodBuilder.addStatement("$L.collect($L)",
                                    modelMirror.collectorField.get(generatedVals).getSimpleName(),
                                    createSubmitterForPresenter(generatedVals, modelMirror,
                                            userMirror));
                        } else {
                            requestMethodBuilder.addStatement("$L.collect($L,$L)",
                                    modelMirror.collectorField.get(generatedVals).getSimpleName(),
                                    createSubmitterForPresenter(generatedVals, modelMirror,
                                            userMirror), paramBlock);
                        }

                        requestMethodBuilder.endControlFlow();
                    }
                }
                requestMethodBuilder.endControlFlow();
            }


        }


        clazzBuilder.addMethod(requestMethodBuilder.build());
    }

    private static void createRequestMethodForCallback(TypeSpec.Builder clazzBuilder,
            KnitModelMirror modelMirror) {

        MethodSpec.Builder requestMethodBuilder = MethodSpec
                .methodBuilder(KnitFileStrings.KNIT_MODEL_REQUEST_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "data")
                .addParameter(ClassName.bestGuess(KnitFileStrings.KNIT_CALLBACK), "callback",
                        Modifier.FINAL)
                .addParameter(Object[].class, "params", Modifier.FINAL);


//        for (String[] generatedVals : modelMirror.generateField.keySet()) {
//            String condition = createConditionBlock(generatedVals);
//            requestMethodBuilder.beginControlFlow("if($L)", condition);
//
//            List<String> types = GeneratorExaminer.getGenerateTypes(
//                    modelMirror.generateField.get(generatedVals));
//
//
//            requestMethodBuilder.addStatement("final $L<$L> genVal = $L.generate($L)",
//                    KnitFileStrings.KNIT_RESPONSE,
//                    types.get(0),
//                    modelMirror.generateField.get(generatedVals).getSimpleName(),
//                    createParamBlock(types, 1));
//            requestMethodBuilder.addStatement("callback.response(genVal)");
//
//            requestMethodBuilder.endControlFlow();
//        }

        for (String[] generatedVals : modelMirror.collectorField.keySet()) {
            String condition = createConditionBlock(generatedVals);
            requestMethodBuilder.beginControlFlow("if($L)", condition);

            List<String> types = GeneratorExaminer.getGenerateTypes(
                    modelMirror.collectorField.get(generatedVals));

            String paramBlock = createParamBlock(types, 1);

            if (paramBlock.equals("")) {
                requestMethodBuilder.addStatement("$L.collect($L)",
                        modelMirror.collectorField.get(generatedVals).getSimpleName(),
                        createSubmitterForCallback(generatedVals, modelMirror));
            } else {
                requestMethodBuilder.addStatement("$L.collect($L,$L)",
                        modelMirror.collectorField.get(generatedVals).getSimpleName(),
                        createSubmitterForCallback(generatedVals, modelMirror),
                        paramBlock);
            }

            requestMethodBuilder.endControlFlow();

        }


        clazzBuilder.addMethod(requestMethodBuilder.build());

    }


    private static String createParamBlock(List<String> params, int padding) {
        StringBuilder paramBuilder = new StringBuilder();

        for (int i = padding; i < params.size(); i++) {
            paramBuilder.append("(");
            paramBuilder.append(params.get(i));
            paramBuilder.append(")");
            paramBuilder.append("params[");
            paramBuilder.append(i - padding);
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


    private static TypeSpec createCallableTypeSpec(String[] params,List<String> types,KnitModelMirror modelMirror){

        ClassName knitResponse = ClassName.bestGuess(KnitFileStrings.KNIT_RESPONSE);
        ClassName responseType = ClassName.bestGuess(types.get(0));
        ParameterizedTypeName parameterizedKnitResponse = ParameterizedTypeName.get(knitResponse,responseType);
        ParameterizedTypeName parameterizedCallable = ParameterizedTypeName.get(KnitFileStrings.TYPE_NAME_CALLABLE,parameterizedKnitResponse);

        TypeSpec callable = TypeSpec
                .anonymousClassBuilder("")
                .addSuperinterface(parameterizedCallable)
                .addMethod(MethodSpec
                        .methodBuilder("call")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(parameterizedKnitResponse)
                        .addStatement("return $L.generate($L)",
                                modelMirror.generatesParamsMap.get(params).variableElement.getSimpleName(),
                                createParamBlock(types, 1))
                        .build())
                .build();

        return callable;
    }

    private static TypeSpec createConsumerTypeSpec(String[] params,List<String> types,KnitModelMirror modelMirror,UserMirror userMirror){
        ClassName knitResponse = ClassName.bestGuess(KnitFileStrings.KNIT_RESPONSE);
        ClassName responseType = ClassName.bestGuess(types.get(0));
        ParameterizedTypeName parameterizedKnitResponse = ParameterizedTypeName.get(knitResponse,responseType);
        ParameterizedTypeName parameterizedConsumerName = ParameterizedTypeName.get(KnitFileStrings.TYPE_NAME_CONSUMER,parameterizedKnitResponse);
        ClassName throwableName = ClassName.bestGuess("java.lang.Throwable");

        TypeSpec consumer = TypeSpec
                .anonymousClassBuilder("")
                .addSuperinterface(parameterizedConsumerName)
                .addMethod(MethodSpec
                        .methodBuilder("consume")
                        .addAnnotation(Override.class)
                        .addParameter(parameterizedKnitResponse,"t")
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("user.$L(t)",
                                findMethod(params, userMirror))
                        .build())
                .addMethod(MethodSpec
                        .methodBuilder("error")
                        .addAnnotation(Override.class)
                        .addParameter(throwableName,"error")
                        .addModifiers(Modifier.PUBLIC).build())
                .build();

        return consumer;

    }


//    private static TypeSpec createAsyncRunnableForPresenter(String[] params,
//            KnitModelMirror modelMirror,
//            UserMirror userMirror) {
//
//        List<String> types = GeneratorExaminer.getGenerateTypes(
//                modelMirror.generateAsyncField.get(params));
//
//        TypeSpec uiRunnable = TypeSpec
//                .anonymousClassBuilder("")
//                .addSuperinterface(Runnable.class)
//                .addMethod(MethodSpec
//                        .methodBuilder("run")
//                        .addAnnotation(Override.class)
//                        .addModifiers(Modifier.PUBLIC)
//                        .addStatement("user.$L(genVal)",
//                                findMethod(params, userMirror))
//                        .build())
//                .build();
//
//
//        return TypeSpec
//                .anonymousClassBuilder("")
//                .addSuperinterface(Runnable.class)
//                .addMethod(MethodSpec
//                        .methodBuilder("run")
//                        .addAnnotation(Override.class)
//                        .addModifiers(Modifier.PUBLIC)
//                        .addStatement("final $L<$L> genVal = $L.generate($L)",
//                                KnitFileStrings.KNIT_RESPONSE,
//                                types.get(0),
//                                modelMirror.generateAsyncField.get(params).getSimpleName(),
//                                createParamBlock(types, 1))
//                        .addStatement("uiThreadHandler.post($L)", uiRunnable)
//                        .build())
//                .build();
//
//    }

//    private static TypeSpec createAsyncRunnableForCallback(String[] params,
//            KnitModelMirror modelMirror) {
//
//        List<String> types = GeneratorExaminer.getGenerateTypes(
//                modelMirror.generateAsyncField.get(params));
//
//        TypeSpec uiRunnable = TypeSpec
//                .anonymousClassBuilder("")
//                .addSuperinterface(Runnable.class)
//                .addMethod(MethodSpec
//                        .methodBuilder("run")
//                        .addAnnotation(Override.class)
//                        .addModifiers(Modifier.PUBLIC)
//                        .addStatement("callback.response(genVal)")
//                        .build())
//                .build();
//
//
//        return TypeSpec
//                .anonymousClassBuilder("")
//                .addSuperinterface(Runnable.class)
//                .addMethod(MethodSpec
//                        .methodBuilder("run")
//                        .addAnnotation(Override.class)
//                        .addModifiers(Modifier.PUBLIC)
//                        .addStatement("final $L<$L> genVal = $L.generate($L)",
//                                KnitFileStrings.KNIT_RESPONSE,
//                                types.get(0),
//                                modelMirror.generateAsyncField.get(params).getSimpleName(),
//                                createParamBlock(types, 1))
//                        .addStatement("uiThreadHandler.post($L)", uiRunnable)
//                        .build())
//                .build();
//
//    }

    private static TypeSpec createSubmitterForPresenter(String[] params,
            KnitModelMirror modelMirror, UserMirror mirror) {

        List<String> types = GeneratorExaminer.getGenerateTypes(
                modelMirror.collectorField.get(params));
        TypeName[] typeNames = new TypeName[types.size()];
        for (int i = 0; i < types.size(); i++) {
            typeNames[i] = ClassName.bestGuess(types.get(i));
        }

        ParameterizedTypeName submitterName = ParameterizedTypeName.get(
                ClassName.bestGuess(KnitFileStrings.KNIT_SUBMITTER), typeNames);

        ParameterizedTypeName responseType = ParameterizedTypeName.get(ClassName.bestGuess(KnitFileStrings.KNIT_RESPONSE),ClassName.bestGuess(types.get(0)));

        return TypeSpec
                .anonymousClassBuilder("")
                .addSuperinterface(submitterName)
                .addMethod(MethodSpec
                        .methodBuilder("submit")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(responseType, "response")
                        .addStatement("user.$L(response)",
                                findMethod(params, mirror))
                        .build())
                .build();


    }

    private static TypeSpec createSubmitterForCallback(String[] params,
            KnitModelMirror modelMirror) {

        List<String> types = GeneratorExaminer.getGenerateTypes(
                modelMirror.collectorField.get(params));
        TypeName[] typeNames = new TypeName[types.size()];
        for (int i = 0; i < types.size(); i++) {
            typeNames[i] = ClassName.bestGuess(types.get(i));
        }

        ParameterizedTypeName submitterName = ParameterizedTypeName.get(
                ClassName.bestGuess(KnitFileStrings.KNIT_SUBMITTER), typeNames);

        ParameterizedTypeName responseType = ParameterizedTypeName.get(ClassName.bestGuess(KnitFileStrings.KNIT_RESPONSE),ClassName.bestGuess(types.get(0)));

        return TypeSpec
                .anonymousClassBuilder("")
                .addSuperinterface(submitterName)
                .addMethod(MethodSpec
                        .methodBuilder("submit")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(responseType, "response")
                        .addStatement("callback.response(response)")
                        .build())
                .build();


    }
}
