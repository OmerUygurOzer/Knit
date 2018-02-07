package com.omerozer.knit;

/**
 * Created by omerozer on 2/1/18.
 */

public interface MemoryEntity {
    void load();
    void destroy();
    boolean shouldLoad();
}
