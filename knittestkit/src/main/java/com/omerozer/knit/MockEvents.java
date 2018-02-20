package com.omerozer.knit;

import com.omerozer.knit.viewevents.KnitOnClickEvent;
import com.omerozer.knit.viewevents.KnitOnClickEventPool;
import com.omerozer.knit.viewevents.KnitOnFocusChangedEventPool;
import com.omerozer.knit.viewevents.KnitOnTextChangedEventPool;

/**
 * Created by omerozer on 2/20/18.
 */

public final class MockEvents {

    private static final KnitOnClickEventPool onClickEventPool = new KnitOnClickEventPool();
    private static final KnitOnTextChangedEventPool onTextChangedEventPool = new KnitOnTextChangedEventPool();
    private static final KnitOnFocusChangedEventPool onFocusChangedEventPool = new KnitOnFocusChangedEventPool();

    public static void fireMockClickEvent(String tag,KnitPresenter knitPresenter){
        KnitOnClickEvent event = onClickEventPool.getEvent();
        event.setTag(tag);
        knitPresenter.handle(onClickEventPool,event,NullValues.NULL_MODEL);
    }

}
