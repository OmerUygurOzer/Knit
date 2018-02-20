package com.omerozer.knit;

/**
 * Created by omerozer on 2/20/18.
 */

public final class ImmutableHolder<T> {

    private T data;

    private final  Object dataLock = new Object();

    public T getData() {
        synchronized (dataLock) {
            return data;
        }
    }

    public void setData(T data) {
        synchronized (dataLock) {
            this.data = data;
        }
    }
}
