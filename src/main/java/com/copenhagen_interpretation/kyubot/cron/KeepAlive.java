package com.copenhagen_interpretation.kyubot.cron;

import com.copenhagen_interpretation.util.GcsUtil;
import com.copenhagen_interpretation.watson.WatsonAssistant;
import com.copenhagen_interpretation.watson.WatsonMapper;
import com.copenhagen_interpretation.watson.model.WatsonMessage;
import com.copenhagen_interpretation.watson.model.WatsonReply;
import com.google.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.UUID;
import java.util.logging.Logger;

public class KeepAlive extends HttpServlet {
    private static final String CONTEXT_FILE = "keepAlive/context.json";

    @Inject
    private static GcsUtil gcsUtil;

    @Inject
    private static Logger logger;

    @Inject
    private static WatsonAssistant watsonAssistant;

    @Inject
    private static WatsonMapper watsonMapper;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String error = null;

        response.setContentType("application/json; charset=utf-8");
        WatsonMessage message = newRandomMessage();
        String reply = watsonAssistant.converse(message);
        if (reply == null) {
            error = "Could not contact Watson.";
            response.setStatus(HttpURLConnection.HTTP_BAD_GATEWAY);
        } else {
            try {
                storeContext(reply);
            } catch (IOException e) {
                error = "Could not store Watson context.";
                response.setStatus(HttpURLConnection.HTTP_UNAVAILABLE);
                logger.severe("Could not store Watson context. The exception is: " + e);
            }
        }

        try {
            if (error != null) {
                reply = new JSONObject().put("error", error).toString();
            }
            response.getWriter().println(reply);
        } catch (IOException | JSONException e) {
            response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
            logger.severe("Couldn't write response. The exception is: " + e);
        }
    }

    private WatsonMessage newRandomMessage() {
        String input = UUID.randomUUID().toString();
        // Check if we have a stored context for the keepAlive conversation.
        String context = null;
        try {
            context = gcsUtil.readContentString(CONTEXT_FILE);
        } catch (IOException e) {
            logger.info("No context found - starting new conversation. " + e);
        }
        return watsonMapper.requestToMessage(input, context);
    }

    private void storeContext(String reply) throws IOException {
        WatsonReply watsonReply = watsonMapper.jsonToReply(reply);
        String context = watsonMapper.toJSON(watsonReply.getContext());
        gcsUtil.saveContent(context, CONTEXT_FILE);
    }

    // Setters are only used by unit tests - dependencies are otherwise injected by Guice
    public static void setGcsUtil(GcsUtil gcsUtil) {
        KeepAlive.gcsUtil = gcsUtil;
    }

    public static void setWatsonAssistant(WatsonAssistant watsonAssistant) {
        KeepAlive.watsonAssistant = watsonAssistant;
    }
}
