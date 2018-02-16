package com.omerozer.knit.generators;

import com.omerozer.knit.KnitResponse;

/**
 * Created by omerozer on 2/4/18.
 */

public interface Generator2<A,T,K> extends ValueGenerator {
    KnitResponse<A> generate(T param1,K param2);
}
