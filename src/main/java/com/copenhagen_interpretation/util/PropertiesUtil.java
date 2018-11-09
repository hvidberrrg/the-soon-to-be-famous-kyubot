package com.copenhagen_interpretation.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final Logger LOGGER = Logger.getLogger(PropertiesUtil.class);

    private PropertiesUtil() {}

    public static Properties getProperties(String propertyFile) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(propertyFile)) {
            LOGGER.debug("Loading properties from '" + propertyFile + "'");
            properties.load(resourceStream);
        } catch (IOException e) {
            LOGGER.error("Could not load properties from '" + propertyFile + "' - " + e);
        }
        return properties;
    }
}
