package com.copenhagen_interpretation.watson.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class SimpleHttpClient {

    public static String doPost(String payload) throws IOException {
        HttpURLConnection connection = prepareConnection();
        sendData(connection, payload);
        // TODO CHECK RESPONSE STATUS CODE
        return receiveResponse(connection);
    }

    private static HttpURLConnection prepareConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(ClientConfig.URL).openConnection();
        connection.setRequestProperty("Authorization", "Basic " + ClientConfig.CREDENTIALS);

        // Prepare for POST request
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        return connection;
    }

    private static void sendData(HttpURLConnection connection, String payload) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
            writer.write(payload);
            writer.flush();
        }
    }
    /*
    private static void sendData(HttpURLConnection connection, String payload) throws IOException {
        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.write(payload.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
    }
     */

    private static String receiveResponse(HttpURLConnection connection) throws IOException {
        String response;
        try (ByteArrayInputStream in = (ByteArrayInputStream) connection.getContent();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            response = reader.lines().collect(Collectors.joining("\n"));
        }

        return response;
    }

    /*
    private static String receiveResponse(HttpURLConnection connection) throws IOException {
        String response;
        try (ByteArrayInputStream in = (ByteArrayInputStream) connection.getContent()) {
            int n = in.available();
            byte[] bytes = new byte[n];
            in.read(bytes, 0, n);
            response = new String(bytes, StandardCharsets.UTF_8);
        }

        return response;
    }
     */
}
