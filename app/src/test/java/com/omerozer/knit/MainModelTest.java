package com.omerozer.knit;

import static junit.framework.Assert.assertEquals;

import com.omerozer.sample.models.MainModel;

import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * Created by omerozer on 2/20/18.
 */

public class MainModelTest extends KnitModelTest<MainModel> {

    MainModel mainModel;

    @Override
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mainModel = getModel();
    }

    @Test
    public void testGenerateTestString() throws Exception{
        final ImmutableHolder<String> immutableHolder = new ImmutableHolder<>();
        accessModelManager().requestThreadSafe("test",immutableHolder);
        assertEquals("TEEEESST STRING",immutableHolder.getData());
    }

    @Override
    protected Class<MainModel> getModelClass() {
        return MainModel.class;
    }
}
