package com.omerozer.knit.collectors;

/**
 * Created by omerozer on 2/14/18.
 */

public interface Collector2<K,T,A>  {
    void collect(Submitter<K> submitter,T param1, A param2);
}
