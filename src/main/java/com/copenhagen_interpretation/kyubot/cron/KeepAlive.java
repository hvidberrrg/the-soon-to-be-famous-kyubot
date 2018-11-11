package com.copenhagen_interpretation.kyubot.cron;

import com.copenhagen_interpretation.util.GcsUtil;
import com.copenhagen_interpretation.watson.WatsonAssistant;
import com.copenhagen_interpretation.watson.WatsonMapper;
import com.copenhagen_interpretation.watson.model.WatsonMessage;
import com.copenhagen_interpretation.watson.model.WatsonReply;
import com.google.inject.Inject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

public class KeepAlive extends HttpServlet {
    private static final String CONTEXT_FILE = "keepAlive/context.json";

    @Inject
    private static Logger logger;

    @Inject
    private static WatsonAssistant watsonAssistant;

    @Inject
    private static WatsonMapper watsonMapper;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WatsonMessage message = newRandomMessage();
        String reply = watsonAssistant.converse(message);
        storeContext(reply);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(reply);
    }

    private WatsonMessage newRandomMessage() {
        String input = UUID.randomUUID().toString();
        // Check if we have a stored context for the keepAlive conversation.
        String context = null;
        try {
            context = GcsUtil.readContentString(CONTEXT_FILE);
        } catch (IOException e) {
            logger.info("No context found - starting new conversation. " + e);
        }
        return watsonMapper.requestToMessage(input, context);
    }

    private void storeContext(String reply) throws IOException {
        WatsonReply watsonReply = watsonMapper.jsonToReply(reply);
        String context = watsonMapper.toJSON(watsonReply.getContext());
        GcsUtil.saveContent(context, CONTEXT_FILE);
    }
}
