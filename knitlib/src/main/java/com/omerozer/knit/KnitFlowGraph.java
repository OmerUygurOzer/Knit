package com.omerozer.knit;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by omerozer on 2/1/18.
 */

class KnitFlowGraph {

    private Map<Class<? extends InternalPresenter>, Set<Class<? extends InternalPresenter>>>
            viewToPresenterMap;

    KnitFlowGraph() {
        this.viewToPresenterMap = new LinkedHashMap<>();
    }

    <T> void mapViewToPresenter(Class<InternalPresenter> from,
            Class<InternalPresenter> to) {
        if (!viewToPresenterMap.containsKey(from)) {
            viewToPresenterMap.put(from, new LinkedHashSet<Class<? extends InternalPresenter>>());
        }
        viewToPresenterMap.get(from).add(to);
    }

    <T> Set<Class<? extends InternalPresenter>> getNext(Class<T> viewClass) {
        return viewToPresenterMap.get(viewClass);
    }


}
