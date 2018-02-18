package com.omerozer.knit.graph;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by omerozer on 2/16/18.
 */

public class EntityNode {
    public int tag;
    public Set<EntityNode> next = new LinkedHashSet<>();
}
