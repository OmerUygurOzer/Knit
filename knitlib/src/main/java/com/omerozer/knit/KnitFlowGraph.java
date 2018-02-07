package com.omerozer.knit;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by omerozer on 2/1/18.
 */

class KnitFlowGraph {

    private Map<Class<? extends KnitPresenter>, Set<Class<? extends KnitPresenter>>>
            viewToPresenterMap;

    KnitFlowGraph() {
        this.viewToPresenterMap = new LinkedHashMap<>();
    }

    <T> void mapViewToPresenter(Class<KnitPresenter> from,
            Class<KnitPresenter> to) {
        if (!viewToPresenterMap.containsKey(from)) {
            viewToPresenterMap.put(from, new LinkedHashSet<Class<? extends KnitPresenter>>());
        }
        viewToPresenterMap.get(from).add(to);
    }

    <T> Set<Class<? extends KnitPresenter>> getNext(Class<T> viewClass) {
        return viewToPresenterMap.get(viewClass);
    }


}
