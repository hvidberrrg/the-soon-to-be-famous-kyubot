package com.copenhagen_interpretation.content;

import com.copenhagen_interpretation.content.model.HtmlPage;
import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import com.copenhagen_interpretation.util.GcsUtil;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(BlockJUnit4ClassRunner.class)
public class ContentMapperTest extends AbstractGuiceInjector {
    @Mock
    private GcsUtil gcsUtilMock;

    @InjectMocks
    @Inject
    private ContentMapper contentMapperWithMocks;

    private static String HTML_PAGE = "/com/copenhagen_interpretation/content/HtmlPage.xml";


    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void getHtmlPageFromURL() throws IOException {
        InputStream htmlIS = new ByteArrayInputStream(testUtil.getFileContents(HTML_PAGE).getBytes(StandardCharsets.UTF_8));
        when(gcsUtilMock.readContent(any(String.class))).thenReturn(htmlIS);

        HtmlPage htmlPage = contentMapperWithMocks.getHtmlPageFromURL("/some/url");
        assertEquals("BREADCRUMB", htmlPage.getBreadcrumb());
        assertEquals("<p>CONTENT</p>", htmlPage.getContent());
        assertEquals("DESCRIPTION", htmlPage.getDescription());
        assertEquals("KEYWORDS", htmlPage.getKeywords());
        assertEquals("TITLE", htmlPage.getTitle());
    }

    @Test
    public void getHtmlPageFromURL_IOException() throws IOException{
        when(gcsUtilMock.readContent(any(String.class))).thenThrow(new IOException("Serious IOException"));

        HtmlPage htmlPage = contentMapperWithMocks.getHtmlPageFromURL("/some/url");
        assertNull(htmlPage);
    }

    @Test
    public void getHtmlPageFromURL_JAXBxception() throws IOException{
        InputStream NotXmlIS = new ByteArrayInputStream("This is not XML!".getBytes(StandardCharsets.UTF_8));
        when(gcsUtilMock.readContent(any(String.class))).thenReturn(NotXmlIS);

        HtmlPage htmlPage = contentMapperWithMocks.getHtmlPageFromURL("/some/url");
        assertNull(htmlPage);
    }
}