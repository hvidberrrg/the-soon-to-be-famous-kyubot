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

    private String credentials;
    private String url;
    private String terminateOnAction;

    @Inject
    public ClientConfig(PropertiesUtil propertiesUtil) {
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
            String credentialsInTheClear = username + ":" + password;
            this.credentials = Base64.getEncoder().encodeToString(credentialsInTheClear.getBytes(StandardCharsets.UTF_8));
            this.terminateOnAction = "end_conversation"; // TODO
        }
    }

    String getCredentials() {
        return credentials;
    }

    String getUrl() {
        return url;
    }

    String getTerminateOnAction() {
        return terminateOnAction;
    }
}


