package com.omerozer.knit.mutators;

/**
 * Created by omerozer on 2/3/18.
 */

public interface Mutator3<A,T,S,K> extends FieldMutator {
    K mutate(A param1, T param2,S param3);
}
