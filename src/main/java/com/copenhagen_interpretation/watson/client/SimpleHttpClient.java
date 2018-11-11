package com.copenhagen_interpretation.watson.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class SimpleHttpClient {

    public String doPost(String payload) throws IOException {
        HttpURLConnection connection = prepareConnection();
        sendData(connection, payload);
        // TODO CHECK RESPONSE STATUS CODE
        return receiveResponse(connection);
    }

    private HttpURLConnection prepareConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(ClientConfig.URL).openConnection();
        connection.setRequestProperty("Authorization", "Basic " + ClientConfig.CREDENTIALS);

        // Prepare for POST request
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        return connection;
    }

    private void sendData(HttpURLConnection connection, String payload) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
            writer.write(payload);
            writer.flush();
        }
    }

    private String receiveResponse(HttpURLConnection connection) throws IOException {
        String response;
        try (ByteArrayInputStream in = (ByteArrayInputStream) connection.getContent();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            response = reader.lines().collect(Collectors.joining("\n"));
        }

        return response;
    }

}
