package com.omerozer.knit.mutators;

/**
 * Created by omerozer on 2/3/18.
 */

public interface Mutator2<A,T,K> extends FieldMutator {
    K mutate(A param1, T param2);
}
