package com.omerozer.knit.componenttests.graphtests;

import com.omerozer.knit.components.graph.SingletonUserCounter;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bo_om on 3/12/2018.
 */

public class SingletonUserCounterTests {

    SingletonUserCounter userCounter;

    @Before
    public void setup(){
        this.userCounter = new SingletonUserCounter();
    }

    @Test
    public void countCycleTest(){
        this.userCounter.use();
        assertEquals(true,userCounter.isUsed());
        this.userCounter.release();
        assertEquals(true,userCounter.isUsed());
        assertEquals(1,userCounter.getCount());
    }

}
