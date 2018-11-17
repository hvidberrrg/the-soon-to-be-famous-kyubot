package com.copenhagen_interpretation.watson;

import com.copenhagen_interpretation.watson.model.Input;
import com.copenhagen_interpretation.watson.model.WatsonMessage;
import com.copenhagen_interpretation.watson.model.WatsonReply;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class WatsonMapper {
    private static final ObjectMapper MAPPER;

    @Inject
    private Logger logger;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.setSerializationInclusion(Include.NON_NULL);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public WatsonMessage requestToMessage(HttpServletRequest request) throws UnsupportedEncodingException {
        String input = request.getParameter("input");
        if (input != null) {
            input = URLDecoder.decode(input, StandardCharsets.UTF_8.name());
        }
        String context = request.getParameter("context");
        if (context != null) {
            context = URLDecoder.decode(context, StandardCharsets.UTF_8.name());
        }

        return requestToMessage(input, context);
    }

    public WatsonMessage requestToMessage(String inputString, String contextString) {
        Input input = new Input(inputString);
        WatsonMessage message = new WatsonMessage(input);
        if (contextString != null && !contextString.isEmpty()) {
            try {
                message.setContext(MAPPER.readTree(contextString));
            } catch (IOException e) {
                logger.severe("Couldn't deserialize context: " + contextString + " - " + e);
            }
        }

        return message;
    }

    public WatsonReply jsonToReply(String json) throws IOException {
        return MAPPER.readValue(json, WatsonReply.class);
    }

    public String toJSON(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }

}
