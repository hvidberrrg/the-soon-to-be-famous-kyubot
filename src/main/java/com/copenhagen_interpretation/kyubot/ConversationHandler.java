package com.copenhagen_interpretation.kyubot;

import com.copenhagen_interpretation.watson.WatsonAssistant;
import com.copenhagen_interpretation.watson.WatsonMapper;
import com.copenhagen_interpretation.watson.model.WatsonMessage;
import com.google.inject.Inject;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class ConversationHandler extends HttpServlet {
    @Inject
    private static Logger logger;

    @Inject
    private static WatsonAssistant watsonAssistant;

    @Inject
    private static WatsonMapper watsonMapper;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String error = null;
        String reply = null;

        response.setContentType("application/json; charset=utf-8");
        WatsonMessage message = getMessageFromRequest(request);
        if (message == null) {
            error = "Could not map request.";
            response.setStatus(HttpURLConnection.HTTP_UNAVAILABLE);
        } else {
            reply = watsonAssistant.converse(message);
            if (reply == null) {
                error = "Could not contact Watson.";
                response.setStatus(HttpURLConnection.HTTP_BAD_GATEWAY);
            }
        }

        try {
            if (error != null) {
                reply = new JSONObject().put("error", error).toString();
            }
            response.getWriter().println(reply);
        } catch (IOException e) {
            response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
            logger.severe("Couldn't write response. The exception is: " + e);
        }
    }

    private WatsonMessage getMessageFromRequest(HttpServletRequest request) {
        try {
            return watsonMapper.requestToMessage(request);
        } catch (UnsupportedEncodingException e) {
            logger.severe("Couldn't map request to Watson message. The exception is: " + e);
            return null;
        }
    }

    // Setters are only used by unit tests - dependencies are otherwise injected by Guice
    public void setWatsonAssistant(WatsonAssistant watsonAssistant) {
        this.watsonAssistant = watsonAssistant;
    }

    public void setWatsonMapper(WatsonMapper watsonMapper) {
        this.watsonMapper = watsonMapper;
    }
}
