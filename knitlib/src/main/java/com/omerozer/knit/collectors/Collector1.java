package com.omerozer.knit.collectors;

/**
 * Created by omerozer on 2/14/18.
 */

public interface Collector1<K,T> {
    void collect(Submitter<K> submitter,T param1);
}
