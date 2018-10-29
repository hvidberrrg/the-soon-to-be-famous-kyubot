package com.copenhagen_interpretation.kyubot;

import com.copenhagen_interpretation.watson.WatsonAssistant;
import com.copenhagen_interpretation.watson.WatsonMapper;
import com.copenhagen_interpretation.watson.model.WatsonMessage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConversationHandler extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WatsonMessage message = WatsonMapper.requestToMessage(request);
        String test = message.toJSON();

        String reply = WatsonAssistant.converse(message);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(reply);
    }
}
