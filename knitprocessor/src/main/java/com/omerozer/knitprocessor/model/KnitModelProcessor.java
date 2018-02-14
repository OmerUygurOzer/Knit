package com.omerozer.knitprocessor.model;

import com.omerozer.knit.Generates;
import com.omerozer.knit.GeneratesAsync;
import com.omerozer.knit.Model;
import com.omerozer.knit.Use;
import com.omerozer.knit.UseMethod;
import com.omerozer.knitprocessor.KnitAnnotations;
import com.omerozer.knitprocessor.user.UserMirror;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

/**
 * Created by omerozer on 2/5/18.
 */

public class KnitModelProcessor extends AbstractProcessor {


    private Set<KnitModelMirror> models;
    private Set<UserMirror> users;
    private Map<KnitModelMirror, Set<UserMirror>> modelToUserMap;
    //public static Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.models = new HashSet<>();
        this.users = new HashSet<>();
        this.modelToUserMap = new HashMap<>();
        //messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Knit Processor Stage3");

        processModels(roundEnvironment.getElementsAnnotatedWith(Model.class));
        processUsers(roundEnvironment.getElementsAnnotatedWith(Use.class));

        handleMapping();

        if(roundEnvironment.processingOver()) {
            createModels(models, modelToUserMap);
            createModelMap(models);
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return KnitAnnotations.getStageThree();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void processModels(Set<? extends Element> models) {
        for (Element model : models) {
            TypeElement clazz = (TypeElement) model;
            KnitModelMirror knitModelMirror = new KnitModelMirror();
            knitModelMirror.enclosingClass = clazz;
            for (Element element : clazz.getEnclosedElements()) {
                if (element.getKind().isField()) {
                    if (element.getAnnotation(Generates.class) != null) {
                        String[] params = element.getAnnotation(Generates.class).value();
                        knitModelMirror.generateField.put(params, (VariableElement) element);
                        knitModelMirror.vals.addAll(Arrays.asList(params));
                    } else if (element.getAnnotation(GeneratesAsync.class) != null) {
                        String[] params = element.getAnnotation(GeneratesAsync.class).value();
                        knitModelMirror.generateAsyncField.put(params, (VariableElement) element);
                        knitModelMirror.vals.addAll(Arrays.asList(params));
                    }
                }
            }
            this.models.add(knitModelMirror);
        }
    }

    private void processUsers(Set<? extends Element> users) {
        for (Element user : users) {
            UserMirror userMirror = new UserMirror();
            userMirror.enclosingClass = (TypeElement) user;
            for (Element element : user.getEnclosedElements()) {
                if (element.getKind().equals(ElementKind.METHOD) && element.getAnnotation(
                        UseMethod.class) != null) {
                    userMirror.method.add((ExecutableElement) element);
                    userMirror.requiredValues.add(element.getAnnotation(UseMethod.class).value());
                }
            }
            this.users.add(userMirror);
        }
    }

    private void handleMapping() {
        for (UserMirror userMirror : users) {
            for (String requiredVal : userMirror.requiredValues) {
                for (KnitModelMirror modelMirror : models) {
                    if (modelMirror.vals.contains(requiredVal)) {
                        if (!modelToUserMap.containsKey(modelMirror)) {
                            modelToUserMap.put(modelMirror, new HashSet<UserMirror>());
                        }
                        modelToUserMap.get(modelMirror).add(userMirror);
                    }
                }
            }
        }
    }

    private void createModels(Set<KnitModelMirror> models,
            Map<KnitModelMirror, Set<UserMirror>> map) {
        for (KnitModelMirror knitModelMirror : models) {
            ModelExposerWriter.write(processingEnv.getFiler(), knitModelMirror);
            KnitModelWriter.write(processingEnv.getFiler(), knitModelMirror, map);
        }
    }

    private void createModelMap(Set<KnitModelMirror> modelsMirror){
        ModelMapWriter.write(processingEnv.getFiler(),modelsMirror);
    }
}
