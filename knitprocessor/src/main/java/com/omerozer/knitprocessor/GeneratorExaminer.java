package com.omerozer.knitprocessor;

import com.omerozer.knitprocessor.model.KnitModelProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

/**
 * Created by omerozer on 2/13/18.
 */

public class GeneratorExaminer {


    public static List<String> getGenerateTypes(VariableElement variableElement){

        String typeString = variableElement.asType().toString();
        KnitModelProcessor.messager.printMessage(Diagnostic.Kind.WARNING,typeString);
        typeString = typeString.substring(typeString.indexOf('<')+1,typeString.lastIndexOf(">"));
        KnitModelProcessor.messager.printMessage(Diagnostic.Kind.WARNING,typeString);
        List<String> params = new ArrayList<>();

        if(!typeString.contains(",")){
            return Arrays.asList(typeString);
        }

        typeString+=",";

        while (typeString.contains(",")){
            params.add(typeString.substring(0,typeString.indexOf(',')));
            typeString = typeString.substring(typeString.indexOf(',')+1,typeString.length());
        }

        return params;
    }


}
