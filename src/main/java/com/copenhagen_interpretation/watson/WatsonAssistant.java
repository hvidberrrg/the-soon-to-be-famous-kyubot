package com.copenhagen_interpretation.watson;

import com.copenhagen_interpretation.watson.client.SimpleHttpClient;
import com.copenhagen_interpretation.watson.model.WatsonMessage;

import java.io.IOException;
import java.util.logging.Logger;

public class WatsonAssistant {
    private static final Logger LOGGER = Logger.getLogger(WatsonAssistant.class.getName());

    private WatsonAssistant() {}

    public static String converse(WatsonMessage message) {
        try {
            return SimpleHttpClient.doPost(message.toJSON());
        } catch (IOException e) {
            LOGGER.severe("Could not converse with Watson. The exception is: " + e);
            return null;
        }
    }
}
