package com.copenhagen_interpretation.util;

import com.google.appengine.tools.cloudstorage.*;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class GcsUtil {
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    private static final String BUCKET = "the-soon-to-be-famous-kyubot.appspot.com";

    // Backoff parameters: do 10 retries within 15 seconds
    private static final GcsService gcsService = GcsServiceFactory.createGcsService(
                                                    new RetryParams.Builder()
                                                            .initialRetryDelayMillis(10)
                                                            .retryMaxAttempts(10)
                                                            .totalRetryPeriodMillis(15000)
                                                            .build());


    public static void saveContent(String content, String filename) throws IOException {
        saveContent(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), filename);
    }

    public static void saveContent(InputStream content, String filename) throws IOException {
        GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
        GcsFilename gcsFilename = new GcsFilename(BUCKET, filename);
        GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsFilename, instance);
        copy(content, Channels.newOutputStream(outputChannel));
    }

    public static String readContentString(String filename) throws IOException {
        try (InputStream in = readContent(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static InputStream readContent(String filename) throws IOException {
        GcsFilename gcsFilename = new GcsFilename(BUCKET, filename);
        GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFilename, 0, BUFFER_SIZE);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(Channels.newInputStream(readChannel), out);

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
        } finally {
            input.close();
            output.close();
        }
    }
}
