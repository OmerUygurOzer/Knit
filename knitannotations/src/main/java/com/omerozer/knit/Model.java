package com.omerozer.knit;

/**
 * Created by omerozer on 2/3/18.
 */

public @interface Model {
    InstanceType value() default InstanceType.IN_GRAPH;
}
