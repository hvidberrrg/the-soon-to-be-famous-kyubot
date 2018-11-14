package com.copenhagen_interpretation.kyubot.cron;

import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import com.copenhagen_interpretation.util.GcsUtil;
import com.copenhagen_interpretation.util.TestUtil;
import com.copenhagen_interpretation.watson.WatsonAssistant;
import com.copenhagen_interpretation.watson.WatsonMapper;
import com.copenhagen_interpretation.watson.model.WatsonMessage;
import com.copenhagen_interpretation.watson.model.WatsonReply;
import com.google.inject.Inject;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class KeepAliveTest extends AbstractGuiceInjector {
    @Inject
    private KeepAlive keepAlive;
    @Inject
    TestUtil testUtil;
    @Inject
    WatsonMapper watsonMapper;

    @Mock
    private GcsUtil mockGcsUtil;
    @Mock
    private HttpServletRequest mockHttpServletRequest;
    @Mock
    private HttpServletResponse mockHttpServletResponse;
    @Mock
    private WatsonAssistant mockWatsonAssistant;

    private StringWriter servletReply = new StringWriter();
    private PrintWriter replyPrintWriter  = new PrintWriter(servletReply);
    private String watsonReplyString;

    private static String REPLY_FILE = "/com/copenhagen_interpretation/watson/watsonMapperTest/watsonReply.json";

    @Before
    public void setup() throws Exception {
        super.setup();
        MockitoAnnotations.initMocks(this);
        // Add mocks to KeepAlive
        KeepAlive.setGcsUtil(mockGcsUtil);
        KeepAlive.setWatsonAssistant(mockWatsonAssistant);
        // Return repsonse contents as the 'reply' StringWriter
        when(mockHttpServletResponse.getWriter()).thenReturn(replyPrintWriter);

        watsonReplyString = testUtil.getFileContents(REPLY_FILE);
    }

    @Test
    public void doPost_couldNotContactWatsonTest() throws Exception{
        JSONObject error = new JSONObject().put("error", "Could not contact Watson.");

        when(mockWatsonAssistant.converse(any(WatsonMessage.class))).thenReturn(null);
        keepAlive.doGet(mockHttpServletRequest, mockHttpServletResponse);
        //assertEquals(HttpURLConnection.HTTP_BAD_GATEWAY, mockHttpServletResponse.getStatus()); // TODO - inlcude when javax.servlet-api is updated to 3.1.0
        assertEquals(error.toString(), servletReply.toString().trim());
    }

    @Test
    public void doPost_couldNotStoreWatsonContextTest() throws Exception{
        JSONObject error = new JSONObject().put("error", "Could not store Watson context.");

        when(mockWatsonAssistant.converse(any(WatsonMessage.class))).thenReturn(watsonReplyString);
        doThrow(IOException.class).when(mockGcsUtil).saveContent(anyString(), anyString());
        keepAlive.doGet(mockHttpServletRequest, mockHttpServletResponse);
        //assertEquals(HttpURLConnection.HTTP_UNAVAILABLE, mockHttpServletResponse.getStatus()); // TODO - inlcude when javax.servlet-api is updated to 3.1.0
        assertEquals(error.toString(), servletReply.toString().trim());
    }

    @Test
    public void doPost_writeFailureTest() throws Exception{
        when(mockWatsonAssistant.converse(any(WatsonMessage.class))).thenReturn(watsonReplyString);
        doAnswer((i) -> {return null;}).when(mockGcsUtil).saveContent(anyString(), anyString());
        when(mockHttpServletResponse.getWriter()).thenThrow(new IOException("Write failure"));
        keepAlive.doGet(mockHttpServletRequest, mockHttpServletResponse);
        //assertEquals(HttpURLConnection.HTTP_INTERNAL_ERROR, mockHttpServletResponse.getStatus()); // TODO - inlcude when javax.servlet-api is updated to 3.1.0
        assertEquals("", servletReply.toString());
    }

    @Test
    public void doPost_noContextFoundTest() throws Exception{
        when(mockGcsUtil.readContentString(anyString())).thenThrow(new IOException("No context found"));
        expectedReply();
    }

    @Test
    public void doPost_test() throws Exception{
        expectedReply();
    }

    private void expectedReply() throws IOException {
        WatsonReply watsonReplyFromFile = watsonMapper.jsonToReply(watsonReplyString);

        when(mockWatsonAssistant.converse(any(WatsonMessage.class))).thenReturn(watsonReplyString);
        doAnswer((i) -> {return null;}).when(mockGcsUtil).saveContent(anyString(), anyString());
        keepAlive.doGet(mockHttpServletRequest, mockHttpServletResponse);
        WatsonReply watsonReply = watsonMapper.jsonToReply(servletReply.toString());
        //assertEquals(HttpURLConnection.HTTP_OK, mockHttpServletResponse.getStatus()); // TODO - inlcude when javax.servlet-api is updated to 3.1.0
        assertEquals(watsonReplyFromFile.getOutput().getText().get(0), watsonReply.getOutput().getText().get(0));
    }
}