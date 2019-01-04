package com.copenhagen_interpretation.content;

import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(BlockJUnit4ClassRunner.class)

public class ContentHandlerUtilTest extends AbstractGuiceInjector {
    private String indexFile = "index.html";
    private String baseUrl = "someDirName";
    private String dirWithTrailingSlash = baseUrl + "/";
    private String dirBeginningWithSlash = "/" + baseUrl;
    private String htmFile = baseUrl + "/someFileName.htm";
    private String htmlFile = baseUrl + "/someFileName.html";
    private String unsupportedUrl = "/kyudo/someFileName.ps";

    @Test
    public void testIsValidUrl() {
        assertTrue(incorrectlyInvalidUrl(baseUrl + "/" + indexFile), contentHandlerUtil.isValidUrl(contentHandlerUtil.normalizeUrl(baseUrl)));
        assertTrue(incorrectlyInvalidUrl(dirWithTrailingSlash + indexFile), contentHandlerUtil.isValidUrl(contentHandlerUtil.normalizeUrl(dirWithTrailingSlash)));
        assertTrue(incorrectlyInvalidUrl(dirBeginningWithSlash + "/" + indexFile), contentHandlerUtil.isValidUrl(contentHandlerUtil.normalizeUrl(dirBeginningWithSlash)));
        assertTrue(incorrectlyInvalidUrl(htmFile + "l"), contentHandlerUtil.isValidUrl(contentHandlerUtil.normalizeUrl(htmFile)));
        assertTrue(incorrectlyInvalidUrl(htmlFile), contentHandlerUtil.isValidUrl(contentHandlerUtil.normalizeUrl(htmlFile)));

        assertFalse(incorrectlyValidUrl(unsupportedUrl), contentHandlerUtil.isValidUrl(contentHandlerUtil.normalizeUrl(unsupportedUrl)));
    }

    @Test
    public void testNormalizeUrl() {
        assertEquals(baseUrl + "/" + indexFile, contentHandlerUtil.normalizeUrl(baseUrl));
        assertEquals(dirWithTrailingSlash + indexFile, contentHandlerUtil.normalizeUrl(dirWithTrailingSlash));
        assertEquals(dirBeginningWithSlash + "/" + indexFile, contentHandlerUtil.normalizeUrl(dirBeginningWithSlash));
        assertEquals(htmFile + "l", contentHandlerUtil.normalizeUrl(htmFile));
        assertEquals(htmlFile, contentHandlerUtil.normalizeUrl(htmlFile));
    }

    private String incorrectlyInvalidUrl(String url) {
        return url + " is considered an invalid url - this is incorrect";
    }

    private String incorrectlyValidUrl(String url) {
        return url + " is considered a valid url - this is incorrect";
    }
}