package com.copenhagen_interpretation.kyubot;

import com.copenhagen_interpretation.content.ContentMapper;
import com.copenhagen_interpretation.content.Freemarker;
import com.copenhagen_interpretation.content.model.HtmlPage;
import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(BlockJUnit4ClassRunner.class)
public class ContentHandlerTest extends AbstractGuiceInjector {
    @Mock
    private ContentMapper mockContenMapper;
    @Mock
    private Freemarker mockFreemarker;
    @Mock
    private HttpServletRequest mockHttpServletRequest;
    @Mock
    private HttpServletResponse mockHttpServletResponse;

    private StringWriter servletReply = new StringWriter();
    private PrintWriter replyPrintWriter  = new PrintWriter(servletReply);

    private static String HTML_PAGE = "/com/copenhagen_interpretation/content/HtmlPage.xml";

    private final LocalServiceTestHelper testHelper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig()
    );

    @Before
    public void setUp() throws Exception {
        super.setUp();
        testHelper.setUp();
        MockitoAnnotations.initMocks(this);
        // Return repsonse contents as the 'reply' StringWriter
        when(mockHttpServletResponse.getWriter()).thenReturn(replyPrintWriter);
        // Let Freemarker know we are running in a test environment
        freemarker.setTemplatePath("src/main/webapp/WEB-INF/templates");
    }

    @After
    public void tearDown() {
        testHelper.tearDown();
    }


    @Test
    public void doGet_test() throws Exception {
        String okUrl = "/test/ok.html";
        gcsUtil.saveContent(testUtil.getFileContents(HTML_PAGE), okUrl);
        when(mockHttpServletRequest.getRequestURI()).thenReturn(okUrl);

        contentHandler.doGet(mockHttpServletRequest, mockHttpServletResponse);
        verify(mockHttpServletResponse).setStatus(HttpURLConnection.HTTP_OK);
    }

    @Test
    public void doGet_templateException() throws Exception {
        // "Inject" static mock ContentMapper
        testUtil.setFinalStaticField(ContentHandler.class, "contentMapper", mockContenMapper);

        String okUrl = "/test/ok.html";
        when(mockHttpServletRequest.getRequestURI()).thenReturn(okUrl);
        when(mockContenMapper.getHtmlPageFromURL(okUrl)).thenReturn(new HtmlPage());

        contentHandler.doGet(mockHttpServletRequest, mockHttpServletResponse);
        verify(mockHttpServletResponse).setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }


    @Test
    public void doGet_unsupportedType() {
        when(mockHttpServletRequest.getRequestURI()).thenReturn("/test/unsupported.ps");

        contentHandler.doGet(mockHttpServletRequest, mockHttpServletResponse);
        verify(mockHttpServletResponse).setStatus(HttpURLConnection.HTTP_UNSUPPORTED_TYPE);
        assertEquals(errorResponse(HttpURLConnection.HTTP_UNSUPPORTED_TYPE), servletReply.toString().trim());

    }

    @Test
    public void doGet_nullTemplate() throws Exception{
        // "Inject" static mock Freemarker
        testUtil.setFinalStaticField(ContentHandler.class, "freemarker", mockFreemarker);

        when(mockHttpServletRequest.getRequestURI()).thenReturn("/test/writeFailure.html");
        when(mockFreemarker.getTemplate(any(String.class))).thenReturn(null);

        contentHandler.doGet(mockHttpServletRequest, mockHttpServletResponse);
        verify(mockHttpServletResponse).setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
        assertEquals(errorResponse(HttpURLConnection.HTTP_INTERNAL_ERROR), servletReply.toString().trim());
    }

    @Test
    public void doGet_ioException() throws Exception{
        when(mockHttpServletRequest.getRequestURI()).thenReturn("/test/writeFailure.html");
        when(mockHttpServletResponse.getWriter()).thenThrow(new IOException("Write failure"));

        contentHandler.doGet(mockHttpServletRequest, mockHttpServletResponse);
        verify(mockHttpServletResponse).setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
        assertEquals("", servletReply.toString().trim());
    }

    private String errorResponse(int statusCode) {
        return "<h1>Error " + statusCode + "</h1>";
    }
}
