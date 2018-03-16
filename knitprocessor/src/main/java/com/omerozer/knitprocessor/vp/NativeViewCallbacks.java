package com.omerozer.knitprocessor.vp;

import java.util.Arrays;
import java.util.List;

/**
 * Created by omerozer on 3/9/18.
 */

public class NativeViewCallbacks {
    public static List<String> getAll(){
        return Arrays.asList("onViewStart","onViewResume","onViewPause","onViewStop","onViewResult","onReturnToView");
    }

    public static boolean isOnViewResult(String result){
        return "onViewResult".equals(result);
    }
}
