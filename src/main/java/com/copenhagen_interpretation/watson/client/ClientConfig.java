package com.copenhagen_interpretation.watson.client;

import com.copenhagen_interpretation.util.PropertiesUtil;
import com.google.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Logger;

public class ClientConfig {
    @Inject
    private Logger logger;

    @Inject
    private PropertiesUtil propertiesUtil;

    private boolean arePropertiesLoaded = false;
    private String credentials;
    private String url;
    private String terminateOnAction;

    private void loadProperties() {
        if (!arePropertiesLoaded) {
            // We can't initialize the properties from a constructor as 'propertiesUtil' need to be available (injected by Guice)
            Properties assistantProps = propertiesUtil.getProperties("assistant.properties");
            if (assistantProps == null) {
                this.credentials = null;
                this.url = null;
                this.terminateOnAction = null;
                logger.severe("Failed to load properties from 'assistant.properties'.");
            } else {
                String username = assistantProps.getProperty("username");
                String password = assistantProps.getProperty("password");
                String apiUrl = assistantProps.getProperty("apiUrl");
                String workspaceId = assistantProps.getProperty("workspaceId");
                String endpoint = assistantProps.getProperty("endpoint");
                String versionDate = assistantProps.getProperty("versionDate");
                this.url = apiUrl + "/" + workspaceId + "/" + endpoint + "?version=" + versionDate;
                String credentials = username + ":" + password;
                this.credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
                this.terminateOnAction = "end_conversation"; // TODO
            }

            arePropertiesLoaded = true;
        }
    }

    public String getCredentials() {
        loadProperties();
        return credentials;
    }

    public String getUrl() {
        loadProperties();
        return url;
    }

    public String getTerminateOnAction() {
        loadProperties();
        return terminateOnAction;
    }
}


