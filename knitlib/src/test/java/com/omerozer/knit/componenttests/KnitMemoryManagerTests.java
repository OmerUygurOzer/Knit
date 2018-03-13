package com.omerozer.knit.componenttests;

import android.content.ComponentCallbacks2;

import com.omerozer.knit.MemoryEntity;
import com.omerozer.knit.components.KnitMemoryManager;
import com.omerozer.knit.components.graph.UsageGraph;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by bo_om on 3/12/2018.
 */

public class KnitMemoryManagerTests {

    KnitMemoryManager knitMemoryManager;

    @Mock
    UsageGraph usageGraph;

    List<MemoryEntity> memoryEntityList;

    @Mock
    MemoryEntity memoryEntity;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.knitMemoryManager = new KnitMemoryManager(usageGraph);
        this.memoryEntityList = new ArrayList<>();
        this.memoryEntityList.add(memoryEntity);
        when(usageGraph.activeEntities()).thenReturn(memoryEntityList);
    }

    @Test
    public void onLowMemoryTest(){
        knitMemoryManager.onLowMemory();
        handleLowMemoryAssertion();
    }

    @Test
    public void onTrimMemoryChange(){
        knitMemoryManager.onTrimMemory(ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW);
        handleLowMemoryAssertion();
    }

    public void handleLowMemoryAssertion(){
        verify(memoryEntity).onMemoryLow();
    }

}
