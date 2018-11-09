package com.copenhagen_interpretation.watson;

import com.copenhagen_interpretation.watson.model.Input;
import com.copenhagen_interpretation.watson.model.WatsonMessage;
import com.copenhagen_interpretation.watson.model.WatsonReply;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class WatsonMapper {
    private static final Logger LOGGER = Logger.getLogger(WatsonMapper.class);
    private static final ObjectMapper MAPPER;
    static {
        MAPPER = new ObjectMapper();
        MAPPER.setSerializationInclusion(Include.NON_NULL);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private WatsonMapper() {}

    public static WatsonMessage requestToMessage(HttpServletRequest request) throws UnsupportedEncodingException {
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

    public static WatsonMessage requestToMessage(String inputString, String contextString) {
        Input input = new Input(inputString);
        WatsonMessage message = new WatsonMessage(input);
        if (contextString != null && !contextString.isEmpty()) {
            try {
                message.setContext(MAPPER.readTree(contextString));
            } catch (IOException e) {
                LOGGER.error("Couldn't deserialize context: " + contextString + " - " + e);
            }
        }

        return message;
    }

    public static WatsonReply jsonToReply(String json) throws IOException {
        return MAPPER.readValue(json, WatsonReply.class);
    }

    public static String toJSON(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }

}
