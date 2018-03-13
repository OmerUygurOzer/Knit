package com.omerozer.knit.components;

 /**
 * Created by omerozer on 2/17/18.
 */

public final class ComponentTag {

    private static Short baseTag = Short.MIN_VALUE;

    public static ComponentTag getNewTag(){
        return new ComponentTag(baseTag++);
    }

    public static void reset(){
        baseTag = Short.MIN_VALUE;
    }

    private Short tag;

    private ComponentTag(Short tag){
        this.tag = tag;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ComponentTag that = (ComponentTag) object;

        return tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }

    public Short getTag(){
        return tag;
    }
}
