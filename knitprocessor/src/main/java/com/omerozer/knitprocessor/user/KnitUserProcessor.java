package com.omerozer.knitprocessor.user;

import com.omerozer.knit.Getter;
import com.omerozer.knit.Use;
import com.omerozer.knit.UseMethod;
import com.omerozer.knitprocessor.KnitAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Created by omerozer on 2/4/18.
 */

public class KnitUserProcessor extends AbstractProcessor {

    List<UserMirror> userMirros;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        userMirros = new ArrayList<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Knit Processor Stage 2");

        processUsers(roundEnvironment.getElementsAnnotatedWith(Use.class));
        createUsers(userMirros);

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return KnitAnnotations.getStageTwo();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
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
                if (element.getKind().equals(ElementKind.METHOD) && element.getAnnotation(
                        Getter.class) != null) {
                    userMirror.getterMap.put(element.getAnnotation(Getter.class).value(),(ExecutableElement)element);
                }
            }
            this.userMirros.add(userMirror);
        }
    }

    private void createUsers(List<UserMirror> users) {
        for (UserMirror userMirror : users) {
            UserWriter.write(processingEnv.getFiler(), userMirror);
        }
    }


}
