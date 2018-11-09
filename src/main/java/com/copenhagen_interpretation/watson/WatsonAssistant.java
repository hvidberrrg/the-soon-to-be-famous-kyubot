package com.copenhagen_interpretation.watson;

import com.copenhagen_interpretation.watson.client.SimpleHttpClient;
import com.copenhagen_interpretation.watson.model.WatsonMessage;

import java.io.IOException;

public class WatsonAssistant {

    private WatsonAssistant() {}

    public static String converse(WatsonMessage message) throws IOException {
         return SimpleHttpClient.doPost(message.toJSON());
    }
}
