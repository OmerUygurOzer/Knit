package com.omerozer.knit.generators;

import com.omerozer.knit.KnitResponse;

/**
 * Created by omerozer on 2/4/18.
 */

public interface Generator1<T,K> extends ValueGenerator {
    KnitResponse<T> generate(K param1);
}
