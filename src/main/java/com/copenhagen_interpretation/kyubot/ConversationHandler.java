package com.copenhagen_interpretation.kyubot;

import com.copenhagen_interpretation.watson.WatsonAssistant;
import com.copenhagen_interpretation.watson.WatsonMapper;
import com.copenhagen_interpretation.watson.model.WatsonMessage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class ConversationHandler extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ConversationHandler.class.getName());

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String error = null;
        String reply = null;

        response.setContentType("application/json; charset=utf-8");
        WatsonMessage message = getMessageFromRequest(request);
        if (message == null) {
            error = "{\"error\": \"Could not map request.\"}";
        } else {
            reply = WatsonAssistant.converse(message);
            if (reply == null) {
                error = "{\"error\": \"Could not contact Watson.\"}";
            }
        }

        if (error != null) {
            response.setStatus(HttpURLConnection.HTTP_UNAVAILABLE);
            reply = error;
        }

        try {
            response.getWriter().println(reply);
        } catch (IOException e) {
            response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
            LOGGER.severe("Couldn't write response. The exception is: " + e);
        }
    }

    private WatsonMessage getMessageFromRequest(HttpServletRequest request) {
        try {
            return WatsonMapper.requestToMessage(request);
        } catch (UnsupportedEncodingException e) {
            LOGGER.severe("Couldn't map request to Watson message. The exception is: " + e);
            return null;
        }
    }

}
