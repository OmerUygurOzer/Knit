package com.omerozer.knit.generators;

import com.omerozer.knit.KnitResponse;

/**
 * Created by omerozer on 2/4/18.
 */

public interface Generator0<K> extends ValueGenerator {
    KnitResponse<K> generate();
}
