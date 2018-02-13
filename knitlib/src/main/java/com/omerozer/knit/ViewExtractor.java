package com.omerozer.knit;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

/**
 * Created by omerozer on 2/12/18.
 */

public class ViewExtractor {

    public View extract(Activity activity){
        return activity.findViewById(android.R.id.content);
    }

    public View extract(View view){
        return view;
    }

    public View extract(Fragment fragment){
        return fragment.getView();
    }

}
