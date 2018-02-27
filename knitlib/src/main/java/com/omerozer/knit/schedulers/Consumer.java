package com.omerozer.knit.schedulers;

/**
 * Created by omerozer on 2/26/18.
 */

public interface Consumer<T> {
    void consume(T t);
    void error(Throwable throwable);
}
