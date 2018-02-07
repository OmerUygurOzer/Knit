package com.omerozer.knit.mutators;

/**
 * Created by omerozer on 2/2/18.
 */

public interface Mutator1<T,K> extends FieldMutator {
    K mutate(T source);
}
