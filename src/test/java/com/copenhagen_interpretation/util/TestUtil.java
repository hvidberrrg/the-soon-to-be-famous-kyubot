package com.copenhagen_interpretation.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class TestUtil {

    public String getFileContents(String filePath) throws IOException {
        URL url = this.getClass().getResource(filePath);
        File contextFile = new File(url.getFile());
        return new String(Files.readAllBytes(contextFile.toPath()), StandardCharsets.UTF_8.name());
    }
}
