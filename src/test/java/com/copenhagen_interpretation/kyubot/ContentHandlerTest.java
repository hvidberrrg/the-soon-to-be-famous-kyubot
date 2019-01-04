package com.copenhagen_interpretation.kyubot;

import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContentHandlerTest extends AbstractGuiceInjector {
    @Mock
    private HttpServletRequest mockHttpServletRequest;
    @Mock
    private HttpServletResponse mockHttpServletResponse;

    private StringWriter servletReply = new StringWriter();
    private PrintWriter replyPrintWriter  = new PrintWriter(servletReply);

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        // Return repsonse contents as the 'reply' StringWriter
        when(mockHttpServletResponse.getWriter()).thenReturn(replyPrintWriter);
    }

    @Test
    public void doGet_test() {
        when(mockHttpServletRequest.getRequestURI()).thenReturn("/test/ok.html");

        contentHandler.doGet(mockHttpServletRequest, mockHttpServletResponse);
        verify(mockHttpServletResponse).setStatus(HttpURLConnection.HTTP_OK);
    }

    @Test
    public void doGet_unsupportedUrl() {
        when(mockHttpServletRequest.getRequestURI()).thenReturn("/test/unsupported.ps");

        contentHandler.doGet(mockHttpServletRequest, mockHttpServletResponse);
        verify(mockHttpServletResponse).setStatus(HttpURLConnection.HTTP_NOT_FOUND);
        assertEquals("<h1>404</h1>", servletReply.toString().trim());

    }

    @Test
    public void doGett_writeFailureTest() throws Exception{
        when(mockHttpServletRequest.getRequestURI()).thenReturn("/test/writeFailure.html");
        when(mockHttpServletResponse.getWriter()).thenThrow(new IOException("Write failure"));

        contentHandler.doGet(mockHttpServletRequest, mockHttpServletResponse);
        verify(mockHttpServletResponse).setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
        assertEquals("", servletReply.toString().trim());
    }
}
