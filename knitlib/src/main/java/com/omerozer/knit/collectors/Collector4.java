package com.omerozer.knit.collectors;

/**
 * Created by omerozer on 2/14/18.
 */

public interface Collector4<K,T,A,S,D> {
    void collect(Submitter<K> submitter,T param1, A param2,S param3, D param4);
}
