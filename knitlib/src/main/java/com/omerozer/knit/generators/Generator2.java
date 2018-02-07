package com.omerozer.knit.generators;

/**
 * Created by omerozer on 2/4/18.
 */

public interface Generator2<A,T,K> extends ValueGenerator {
    K generate(T param1,K param2);
}
