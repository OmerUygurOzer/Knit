package com.omerozer.knit.componenttests.graphtests;

import com.omerozer.knit.KnitInterface;
import com.omerozer.knit.KnitMock;
import com.omerozer.knit.View1;
import com.omerozer.knit.components.ComponentTag;
import com.omerozer.knit.components.graph.UsageGraph;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Omer Ozer on 3/12/2018.
 */

public class UsageGraphTests {

    KnitInterface knit;

    UsageGraph usageGraph;


    @Before
    public void setup(){
        ComponentTag.reset();
        this.knit = KnitMock.get();
        this.usageGraph = new UsageGraph(knit);
    }

    @Test
    public void graphInitializationTest(){
        Short cur = ComponentTag.getNewTag().getTag();
        assertEquals(Short.valueOf("-32762"),cur);
    }

    @Test
    public void startViewComponentTest(){
        usageGraph.startViewAndItsComponents(new View1(),null);

    }

}
