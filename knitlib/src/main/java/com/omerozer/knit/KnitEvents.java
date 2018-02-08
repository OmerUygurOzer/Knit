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
                Knit.findPresenter(carrierObject);
                KnitOnClickEvent event = onClickEventPool.getEvent();
                event.setTag(tag);
                event.setViewWeakReference(view);
                KnitModel modelManager = Knit.findPresenter(carrierObject).getModelManager();
                Knit.findPresenter(carrierObject).handle(onClickEventPool,event,modelManager);
            }
        });
    }
}
