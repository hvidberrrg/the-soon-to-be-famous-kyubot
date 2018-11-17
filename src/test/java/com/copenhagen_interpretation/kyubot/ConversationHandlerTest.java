package com.copenhagen_interpretation.kyubot;

import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import com.copenhagen_interpretation.watson.WatsonAssistant;
import com.copenhagen_interpretation.watson.WatsonMapper;
import com.copenhagen_interpretation.watson.model.WatsonMessage;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ConversationHandlerTest extends AbstractGuiceInjector {
    @Mock
    private HttpServletRequest mockHttpServletRequest;
    @Mock
    private HttpServletResponse mockHttpServletResponse;
    @Mock
    private WatsonAssistant mockWatsonAssistant;
    @Mock
    private WatsonMapper mockWatsonMapper;

    private StringWriter servletReply = new StringWriter();
    private PrintWriter replyPrintWriter  = new PrintWriter(servletReply);

    private WatsonMessage message;

    @Before
    public void setup() throws Exception {
        super.setup();
        MockitoAnnotations.initMocks(this);
        // Add mocks to ConversationHandler
        ConversationHandler.setWatsonAssistant(mockWatsonAssistant);
        ConversationHandler.setWatsonMapper(mockWatsonMapper);
        // Return repsonse contents as the 'reply' StringWriter
        when(mockHttpServletResponse.getWriter()).thenReturn(replyPrintWriter);

        message = watsonMapper.requestToMessage("message", null);
    }

    @Test
    public void doPost_MappingFailedTest() throws Exception{
        JSONObject error = new JSONObject().put("error", "Could not map request.");

        when(mockWatsonMapper.requestToMessage(any(HttpServletRequest.class))).thenThrow(new UnsupportedEncodingException("arrrgh"));
        conversationHandler.doPost(mockHttpServletRequest, mockHttpServletResponse);
        //assertEquals(HttpURLConnection.HTTP_UNAVAILABLE, mockHttpServletResponse.getStatus()); // TODO - inlcude when javax.servlet-api is updated to 3.1.0
        assertEquals(error.toString(), servletReply.toString().trim());
    }

    @Test
    public void doPost_couldNotContactWatsonTest() throws Exception{
        JSONObject error = new JSONObject().put("error", "Could not contact Watson.");

        when(mockWatsonMapper.requestToMessage(any(HttpServletRequest.class))).thenReturn(message);
        when(mockWatsonAssistant.converse(any(WatsonMessage.class))).thenReturn(null);
        conversationHandler.doPost(mockHttpServletRequest, mockHttpServletResponse);
        //assertEquals(HttpURLConnection.HTTP_BAD_GATEWAY, mockHttpServletResponse.getStatus()); // TODO - inlcude when javax.servlet-api is updated to 3.1.0
        assertEquals(error.toString(), servletReply.toString().trim());
    }

    @Test
    public void doPost_writeFailureTest() throws Exception{
        JSONObject testReply = new JSONObject().put("fromWatson", "With love.");

        when(mockWatsonMapper.requestToMessage(any(HttpServletRequest.class))).thenReturn(message);
        when(mockWatsonAssistant.converse(any(WatsonMessage.class))).thenReturn(testReply.toString());
        when(mockHttpServletResponse.getWriter()).thenThrow(new IOException("Write failure"));
        conversationHandler.doPost(mockHttpServletRequest, mockHttpServletResponse);
        //assertEquals(HttpURLConnection.HTTP_INTERNAL_ERROR, mockHttpServletResponse.getStatus()); // TODO - inlcude when javax.servlet-api is updated to 3.1.0
        assertEquals("", servletReply.toString().trim());
    }

    @Test
    public void doPost_test() throws Exception{
        JSONObject testReply = new JSONObject().put("fromWatson", "With love.");

        when(mockWatsonMapper.requestToMessage(any(HttpServletRequest.class))).thenReturn(message);
        when(mockWatsonAssistant.converse(any(WatsonMessage.class))).thenReturn(testReply.toString());
        conversationHandler.doPost(mockHttpServletRequest, mockHttpServletResponse);
        //assertEquals(HttpURLConnection.HTTP_OK, mockHttpServletResponse.getStatus()); // TODO - inlcude when javax.servlet-api is updated to 3.1.0
        assertEquals(testReply.toString(), servletReply.toString().trim());
    }
}