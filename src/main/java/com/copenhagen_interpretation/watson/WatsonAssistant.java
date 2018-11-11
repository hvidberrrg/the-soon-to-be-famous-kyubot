package com.copenhagen_interpretation.watson;

import com.copenhagen_interpretation.watson.client.SimpleHttpClient;
import com.copenhagen_interpretation.watson.model.WatsonMessage;
import com.google.inject.Inject;

import java.io.IOException;
import java.util.logging.Logger;

public class WatsonAssistant {
    @Inject
    private Logger logger;

    @Inject
    private SimpleHttpClient simpleHttpClient;

    @Inject
    private WatsonMapper watsonMapper;

    public String converse(WatsonMessage message) {
        try {
            return simpleHttpClient.doPost(watsonMapper.toJSON(message));
        } catch (IOException e) {
            logger.severe("Could not converse with Watson. The exception is: " + e);
            return null;
        }
    }
}
