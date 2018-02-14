package com.omerozer.knit.collectors;

/**
 * Created by omerozer on 2/14/18.
 */

public interface Submitter<K> {
    void submit(K response);
}
