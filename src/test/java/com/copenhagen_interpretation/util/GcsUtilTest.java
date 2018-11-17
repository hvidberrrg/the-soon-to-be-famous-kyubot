package com.copenhagen_interpretation.util;

import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class GcsUtilTest extends AbstractGuiceInjector {

    private final LocalServiceTestHelper testHelper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig()
    );

    private static final String FILENAME = "testFilename";
    private static final String CONTENT = "Test";

    @Before
    public void setup() throws Exception {
        super.setup();
        testHelper.setUp();
    }

    @After
    public void tearDown() {
        testHelper.tearDown();
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void testSaveContent() throws IOException {
        gcsUtil.saveContent(new ByteArrayInputStream(CONTENT.getBytes(StandardCharsets.UTF_8)), FILENAME);
    }

    @Test
    public void testReadContentString() throws IOException {
        gcsUtil.saveContent(new ByteArrayInputStream(CONTENT.getBytes(StandardCharsets.UTF_8)), FILENAME);
        assertEquals(CONTENT, gcsUtil.readContentString(FILENAME));
    }
}
