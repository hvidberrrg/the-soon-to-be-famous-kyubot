package com.copenhagen_interpretation.kyubot.cron;

import com.copenhagen_interpretation.util.GcsUtil;
import com.copenhagen_interpretation.watson.WatsonAssistant;
import com.copenhagen_interpretation.watson.WatsonMapper;
import com.copenhagen_interpretation.watson.model.WatsonMessage;
import com.copenhagen_interpretation.watson.model.WatsonReply;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class KeepAlive extends HttpServlet {
    private static final String CONTEXT_FILE = "keepAlive/context.json";
    private static final Logger LOGGER = Logger.getLogger(KeepAlive.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WatsonMessage message = newRandomMessage();
        String reply = WatsonAssistant.converse(message);
        storeContext(reply);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(reply);
    }

    private WatsonMessage newRandomMessage() throws IOException {
        String input = UUID.randomUUID().toString();
        // Check if we have a stored context for the keepAlive conversation.
        String context = null;
        try {
            context = GcsUtil.readContentString(CONTEXT_FILE);
        } catch (FileNotFoundException e) {
            LOGGER.warn("No context found - starting new conversation. " + e);
        }
        return WatsonMapper.requestToMessage(input, context);
    }

    private void storeContext(String reply) throws IOException {
        WatsonReply watsonReply = WatsonMapper.jsonToReply(reply);
        String context = WatsonMapper.toJSON(watsonReply.getContext());
        GcsUtil.saveContent(context, CONTEXT_FILE);
    }
}
