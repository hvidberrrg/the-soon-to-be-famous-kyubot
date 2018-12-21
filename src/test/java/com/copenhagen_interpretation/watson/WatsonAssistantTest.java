package com.copenhagen_interpretation.watson;

import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import com.copenhagen_interpretation.watson.client.SimpleHttpClient;
import com.copenhagen_interpretation.watson.model.Input;
import com.copenhagen_interpretation.watson.model.WatsonMessage;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class WatsonAssistantTest extends AbstractGuiceInjector {
    @Mock
    private SimpleHttpClient simpleHttpClientMock;
    @InjectMocks
    @Inject
    private WatsonAssistant watsonAssistantWithMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConverse_OK() throws IOException {
        when(simpleHttpClientMock.doPost(any(String.class))).thenReturn("helloWorld");

        assertEquals("helloWorld", watsonAssistantWithMock.converse(new WatsonMessage(new Input("hey there Watson"))));
    }

    @Test
    public void testConverse_Fail() throws IOException {
        when(simpleHttpClientMock.doPost(any(String.class))).thenThrow(new IOException("Couldn't communicate with Watson"));

        assertNull(watsonAssistantWithMock.converse(new WatsonMessage(new Input("hey there Watson"))));
    }
}