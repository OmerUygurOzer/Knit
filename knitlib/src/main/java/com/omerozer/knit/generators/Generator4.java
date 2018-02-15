package com.omerozer.knit.generators;

/**
 * Created by omerozer on 2/4/18.
 */

public interface Generator4<A,T,S,D,K> extends ValueGenerator {
    A generate(T param1, S param2,D param3,K param4);
}
