package com.copenhagen_interpretation.watson.client;

import com.copenhagen_interpretation.util.PropertiesUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

final class ClientConfig {
    static final String CREDENTIALS;
    static final String URL;
    static final String TERMINATE_ON_ACTION;

    static {
        Properties assistantProps = PropertiesUtil.getProperties("assistant.properties");
        String username = assistantProps.getProperty("username");
        String password = assistantProps.getProperty("password");
        String apiUrl = assistantProps.getProperty("apiUrl");
        String workspaceId = assistantProps.getProperty("workspaceId");
        String endpoint = assistantProps.getProperty("endpoint");
        String versionDate = assistantProps.getProperty("versionDate");
        URL = apiUrl + "/" + workspaceId + "/" + endpoint + "?version=" + versionDate;
        String credentials = username + ":" + password;
        CREDENTIALS = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        TERMINATE_ON_ACTION = "TODO"; // TODO
    }

    private ClientConfig() {}
}


