package com.omerozer.knit;

/**
 * Created by omerozer on 3/16/18.
 */

public class MessagePool extends MemoryPool<KnitMessage> {
    @Override
    protected KnitMessage createNewInstance() {
        return new KnitMessage();
    }
}
