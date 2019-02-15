package com.copenhagen_interpretation.content;

import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class ContentUtilTest extends AbstractGuiceInjector {
    private String indexFile = "index.html";
    private String baseUrl = "someDirName";
    private String dirWithTrailingSlash = baseUrl + "/";
    private String dirBeginningWithSlash = "/" + baseUrl;
    private String htmFile = baseUrl + "/someFileName.htm";
    private String htmlFile = baseUrl + "/someFileName.html";
    private String unsupportedUrl = "/kyudo/someFileName.ps";

    @Test
    public void testIsValidUrl() {
        assertTrue(incorrectlyInvalidUrl(baseUrl + "/" + indexFile), contentUtil.isValidUrl(contentUtil.normalizeUrl(baseUrl)));
        assertTrue(incorrectlyInvalidUrl(dirWithTrailingSlash + indexFile), contentUtil.isValidUrl(contentUtil.normalizeUrl(dirWithTrailingSlash)));
        assertTrue(incorrectlyInvalidUrl(dirBeginningWithSlash + "/" + indexFile), contentUtil.isValidUrl(contentUtil.normalizeUrl(dirBeginningWithSlash)));
        assertTrue(incorrectlyInvalidUrl(htmFile + "l"), contentUtil.isValidUrl(contentUtil.normalizeUrl(htmFile)));
        assertTrue(incorrectlyInvalidUrl(htmlFile), contentUtil.isValidUrl(contentUtil.normalizeUrl(htmlFile)));

        assertFalse(incorrectlyValidUrl(unsupportedUrl), contentUtil.isValidUrl(contentUtil.normalizeUrl(unsupportedUrl)));
    }

    @Test
    public void testNormalizeUrl() {
        assertEquals(baseUrl + "/" + indexFile, contentUtil.normalizeUrl(baseUrl));
        assertEquals(dirWithTrailingSlash + indexFile, contentUtil.normalizeUrl(dirWithTrailingSlash));
        assertEquals(dirBeginningWithSlash + "/" + indexFile, contentUtil.normalizeUrl(dirBeginningWithSlash));
        assertEquals(htmFile + "l", contentUtil.normalizeUrl(htmFile));
        assertEquals(htmlFile, contentUtil.normalizeUrl(htmlFile));
    }

    private String incorrectlyInvalidUrl(String url) {
        return url + " is considered an invalid url - this is incorrect";
    }

    private String incorrectlyValidUrl(String url) {
        return url + " is considered a valid url - this is incorrect";
    }
}