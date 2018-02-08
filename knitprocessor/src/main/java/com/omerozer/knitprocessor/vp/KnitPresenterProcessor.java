package com.omerozer.knitprocessor.vp;

import com.omerozer.knit.KnitView;
import com.omerozer.knit.Leech;
import com.omerozer.knit.Mutator;
import com.omerozer.knit.Presenter;
import com.omerozer.knit.Seed;
import com.omerozer.knit.Updating;
import com.omerozer.knit.ViewEventHandler;
import com.omerozer.knitprocessor.KnitAnnotations;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * Created by omerozer on 2/2/18.
 */

public class KnitPresenterProcessor extends AbstractProcessor {

    private Set<KnitPresenterMirror> presenters;
    private Set<KnitViewMirror> views;
    private Map<KnitPresenterMirror, KnitViewMirror> presenterToViewMap;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.presenters = new LinkedHashSet<>();
        this.views = new LinkedHashSet<>();
        this.presenterToViewMap = new LinkedHashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Knit Processor Stage1");

        processViews(roundEnvironment.getElementsAnnotatedWith(KnitView.class));
        processPresenters(roundEnvironment.getElementsAnnotatedWith(Presenter.class));

        handleMapping();


        createViews(views);
        createPresenters(presenters, presenterToViewMap);
        createViewToPresenterMap(presenterToViewMap);


        return true;
    }

    private void processPresenters(Set<? extends Element> presenters) {
        for (Element presenter : presenters) {
            TypeElement clazz = (TypeElement) presenter;
            KnitPresenterMirror knitPresenterMirror = new KnitPresenterMirror();
            knitPresenterMirror.enclosingClass = clazz;
            knitPresenterMirror.targetView = getClassAnnotationValue(presenter, Presenter.class);
            for (Element element : clazz.getEnclosedElements()) {
                if (element.getKind().isField()) {
                    if (element.getAnnotation(Seed.class) != null) {
                        knitPresenterMirror.seedingFields.put(
                                element.getAnnotation(Seed.class).value(),
                                (VariableElement) element);
                    } else if (element.getAnnotation(Mutator.class) != null) {
                        String fieldTag = element.getAnnotation(Mutator.class).value();
                        knitPresenterMirror.mutatorFields.put(fieldTag, (VariableElement) element);
                        String[] params = element.getAnnotation(Mutator.class).params();

                        if (params[0].equals("")) {
                            knitPresenterMirror.mutatorParams.put(fieldTag, new String[]{fieldTag});
                        } else {
                            knitPresenterMirror.mutatorParams.put(fieldTag, params);
                        }
                    } else if (element.getAnnotation(ViewEventHandler.class) != null) {
                        String fieldTag = element.getAnnotation(ViewEventHandler.class).value();
                        knitPresenterMirror.eventHandlerFields.put(fieldTag,
                                (VariableElement) element);
                    }
                }
                this.presenters.add(knitPresenterMirror);
            }
        }
    }

    private void processViews(Set<? extends Element> presenters) {
        for (Element presenter : presenters) {
            TypeElement clazz = (TypeElement) presenter;
            KnitViewMirror knitViewMirror = new KnitViewMirror();
            knitViewMirror.enclosingClass = clazz;
            for (Element element : clazz.getEnclosedElements()) {
                if (element.getKind().isField() && element.getAnnotation(Leech.class) != null) {
                    knitViewMirror.leechingFields.put(element.getAnnotation(Leech.class).value(),
                            (VariableElement) element);
                } else if (element.getKind() == ElementKind.METHOD && element.getAnnotation(
                        Updating.class) != null) {
                    String fieldTag = element.getAnnotation(Updating.class).value();
                    knitViewMirror.updatingMethods.put(fieldTag, (ExecutableElement) element);
                }
            }
            this.views.add(knitViewMirror);
        }
    }

    private void handleMapping() {
        for (KnitPresenterMirror presenter : presenters) {
            for (KnitViewMirror views : views) {
                if (presenter.targetView.equals(views.enclosingClass.asType())) {
                    presenterToViewMap.put(presenter, views);
                }
            }
        }
    }


    private TypeMirror getClassAnnotationValue(Element element, Class<?> clazz) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            if (mirror.getAnnotationType().toString().equals(clazz.getName())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
                        mirror.getElementValues().entrySet()) {
                    if (entry.getKey().getSimpleName().toString().equals("value")) {
                        return (TypeMirror) entry.getValue().getValue();
                    }
                }

            }
        }
        return null;
    }

    private void createPresenters(Set<KnitPresenterMirror> presenters,
            Map<KnitPresenterMirror, KnitViewMirror> map) {
        for (KnitPresenterMirror knitPresenterMirror : presenters) {
            PresenterExposerWriter.write(processingEnv.getFiler(), knitPresenterMirror);
            KnitPresenterWriter.write(processingEnv.getFiler(), knitPresenterMirror, map);
        }
    }

    private void createViewToPresenterMap(Map<KnitPresenterMirror, KnitViewMirror> map) {
        ViewToPresenterMapWriter.write(processingEnv.getFiler(), map);
    }


    private void createViews(Set<KnitViewMirror> views) {

    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return KnitAnnotations.getStageOne();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


}
