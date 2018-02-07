package com.omerozer.knitprocessor;

import com.omerozer.knitprocessor.vp.KnitPresenterMirror;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

/**
 * Created by omerozer on 2/4/18.
 */

public class MutatorTypeNameCreator {

    private static String MUTATOR1 = "com.omerozer.knit.mutators.Mutator1";
    private static String MUTATOR2 = "com.omerozer.knit.mutators.Mutator2";
    private static String MUTATOR3 = "com.omerozer.knit.mutators.Mutator3";
    private static String MUTATOR4 = "com.omerozer.knit.mutators.Mutator4";

    public static ParameterizedTypeName create(String string,KnitPresenterMirror knitPresenterMirror){
        ParameterizedTypeName name = null;

        TypeName seedName = Boxer.boxIfNeeded(knitPresenterMirror.seedingFields.get(string));
        String[] params = knitPresenterMirror.mutatorParams.get(string);
        TypeName param1;
        TypeName param2;
        TypeName param3;
        TypeName param4;

        if(params[0].equals("")){
            return ParameterizedTypeName.get(ClassName.bestGuess(MUTATOR1),seedName,seedName);
        }

        switch (params.length){
            case 1:
                param1 = Boxer.boxIfNeeded(knitPresenterMirror.seedingFields.get(params[0]));
                name = ParameterizedTypeName.get(ClassName.bestGuess(MUTATOR1),seedName,param1);
                break;

            case 2:
                param1 = Boxer.boxIfNeeded(knitPresenterMirror.seedingFields.get(params[0]));
                param2 = Boxer.boxIfNeeded(knitPresenterMirror.seedingFields.get(params[1]));
                name = ParameterizedTypeName.get(ClassName.bestGuess(MUTATOR2),seedName,param1,param2);
                break;

            case 3:
                param1 = Boxer.boxIfNeeded(knitPresenterMirror.seedingFields.get(params[0]));
                param2 = Boxer.boxIfNeeded(knitPresenterMirror.seedingFields.get(params[1]));
                param3 = Boxer.boxIfNeeded(knitPresenterMirror.seedingFields.get(params[2]));
                name = ParameterizedTypeName.get(ClassName.bestGuess(MUTATOR3),seedName,param1,param2,param3);
                break;

            case 4:
                param1 = Boxer.boxIfNeeded(knitPresenterMirror.seedingFields.get(params[0]));
                param2 = Boxer.boxIfNeeded(knitPresenterMirror.seedingFields.get(params[1]));
                param3 = Boxer.boxIfNeeded(knitPresenterMirror.seedingFields.get(params[2]));
                param4 = Boxer.boxIfNeeded(knitPresenterMirror.seedingFields.get(params[3]));
                name = ParameterizedTypeName.get(ClassName.bestGuess(MUTATOR4),seedName,param1,param2,param3,param4);
                break;
        }


        return name;
    }
}
