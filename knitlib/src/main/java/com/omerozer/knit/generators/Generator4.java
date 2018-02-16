package com.omerozer.knit.generators;

import com.omerozer.knit.KnitResponse;

/**
 * Created by omerozer on 2/4/18.
 */

public interface Generator4<A,T,S,D,K> extends ValueGenerator {
    KnitResponse<A> generate(T param1, S param2,D param3,K param4);
}
