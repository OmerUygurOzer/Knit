package com.omerozer.knit.generators;

/**
 * Created by omerozer on 2/4/18.
 */

public interface Generator3<A,T,S,K> extends ValueGenerator {
    K generate(A param1, T param2,S param3);
}
