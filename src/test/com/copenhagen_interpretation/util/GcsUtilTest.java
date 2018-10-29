package com.copenhagen_interpretation.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class GcsUtilTest {
    @Test
    public void testSaveContent() throws IOException {
        GcsUtil.saveContent(new ByteArrayInputStream("TEST".getBytes(StandardCharsets.UTF_8)), "testFilename");
    }
}
