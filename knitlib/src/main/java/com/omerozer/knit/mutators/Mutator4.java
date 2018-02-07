package com.omerozer.knit.mutators;

/**
 * Created by omerozer on 2/3/18.
 */

public interface Mutator4<A,T,S,D,K> extends FieldMutator {
    K mutate(A param1, T param2,S param3,D param4);
}
