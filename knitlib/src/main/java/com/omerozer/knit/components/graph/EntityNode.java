package com.omerozer.knit.components.graph;

import com.omerozer.knit.components.ComponentTag;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by omerozer on 2/16/18.
 */

public class EntityNode {
    public ComponentTag tag;
    public EntityType type;
    public Set<EntityNode> next = new LinkedHashSet<>();
    public EntityNode(ComponentTag componentTag,EntityType entityType){
        this.tag = componentTag;
        this.type = entityType;
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }
}
