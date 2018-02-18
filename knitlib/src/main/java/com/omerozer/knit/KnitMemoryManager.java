package com.omerozer.knit;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by omerozer on 2/1/18.
 */

public class KnitMemoryManager implements ComponentCallbacks2 {

    private Set<MemoryEntity> entities;

    KnitMemoryManager(Context context) {
        this.entities = new LinkedHashSet<>();
    }

    void registerInstance(MemoryEntity entity) {
        this.entities.add(entity);
    }

    void evictEntity(MemoryEntity entity) {
        this.entities.remove(entity);
    }

    boolean contains(MemoryEntity entity) {
        return entities.contains(entity);
    }

    @Override
    public void onTrimMemory(int i) {
        handleMemoryLevel(i);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onLowMemory() {
        handleMemoryLevel(ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW);
    }

    private void handleMemoryLevel(int i) {
        if (i == ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
            for (MemoryEntity entity : entities) {
                entity.onMemoryLow();
            }
        }
    }
}
