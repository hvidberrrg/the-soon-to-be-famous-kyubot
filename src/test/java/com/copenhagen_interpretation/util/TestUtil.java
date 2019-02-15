package com.copenhagen_interpretation.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class TestUtil {

    public String getFileContents(String filePath) throws IOException {
        URL url = this.getClass().getResource(filePath);
        if (url == null) {
            return "getResource returned null";
        } else {
            File contextFile = new File(url.getFile());
            return new String(Files.readAllBytes(contextFile.toPath()), StandardCharsets.UTF_8.name());
        }
    }

    // Allow us to change what must not be changed. Strictly for testing purposes
    public void setFinalStaticField(Class<?> clazz, String fieldName, Object value) throws ReflectiveOperationException {
        Field field = clazz.getDeclaredField(fieldName);
        // Make the field accessible
        field.setAccessible(true);
        // Remove the "final" modifier if present
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        // Assign the new value to the field
        field.set(null, value);
    }
}
