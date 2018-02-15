package com.omerozer.knit.generators;

/**
 * Created by omerozer on 2/4/18.
 */

public interface Generator1<T,K> extends ValueGenerator {
    T generate(K param1);
}
