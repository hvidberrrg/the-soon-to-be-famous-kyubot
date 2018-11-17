package com.copenhagen_interpretation.util;

import com.google.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertiesUtil {
    @Inject
    private Logger logger;

    public Properties getProperties(String propertyFile) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            logger.severe("Could not get class loader.");
            return null;
        } else {
            Properties properties = new Properties();
            try (InputStream resourceStream = loader.getResourceAsStream(propertyFile)) {
                logger.info("Loading properties from '" + propertyFile + "'");
                properties.load(resourceStream);
            } catch (IOException e) {
                logger.severe("Could not load properties from '" + propertyFile + "' - " + e);
            }
            return properties;
        }
    }
}
