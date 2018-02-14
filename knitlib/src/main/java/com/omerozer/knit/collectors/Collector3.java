package com.omerozer.knit.collectors;

/**
 * Created by omerozer on 2/14/18.
 */

public interface Collector3<K,T,A,S> {
    void collect(Submitter<K> submitter,T param1, A param2,S param3);
}
