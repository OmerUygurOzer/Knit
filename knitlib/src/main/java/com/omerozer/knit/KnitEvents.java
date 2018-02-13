package com.omerozer.knit;

import android.view.View;

import com.omerozer.knit.viewevents.KnitOnClickEvent;
import com.omerozer.knit.viewevents.KnitOnClickEventPool;

/**
 * Created by omerozer on 2/2/18.
 */

public class KnitEvents {

    private static KnitOnClickEventPool onClickEventPool       = new KnitOnClickEventPool();
    private static KnitOnClickEventPool onTextChangedEventPool = new KnitOnClickEventPool();


    public static void onClick(final String tag, final Object carrierObject,View view){
        view.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                KnitOnClickEvent event = onClickEventPool.getEvent();
                event.setTag(tag);
                event.setViewWeakReference(view);
                InternalModel modelManager = Knit.findPresenterForView(carrierObject).getModelManager();
                Knit.findPresenterForView(carrierObject).handle(onClickEventPool,event,modelManager);
            }
        });
    }

}
