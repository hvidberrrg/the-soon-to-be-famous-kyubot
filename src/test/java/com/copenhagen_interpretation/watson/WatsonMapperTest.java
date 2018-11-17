package com.copenhagen_interpretation.watson;

import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import com.copenhagen_interpretation.util.TestUtil;
import com.copenhagen_interpretation.watson.model.WatsonMessage;
import com.copenhagen_interpretation.watson.model.WatsonReply;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(BlockJUnit4ClassRunner.class)
public class WatsonMapperTest extends AbstractGuiceInjector {
    private static String CONTEXT_FILE = "/com/copenhagen_interpretation/watson/watsonMapperTest/context.json";
    private static String REPLY_FILE = "/com/copenhagen_interpretation/watson/watsonMapperTest/watsonReply.json";
    private static String INPUT_TEXT = "Input text";

    @Inject
    private TestUtil testUtil;
    @Inject
    private WatsonMapper watsonMapper;

    @Mock
    private HttpServletRequest mockHttpServletRequest;

    @Before
    public void setup() throws Exception {
        super.setup();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRequestToMessage_EmptyContext() {
        WatsonMessage message = watsonMapper.requestToMessage(INPUT_TEXT, "");

        assertEquals(INPUT_TEXT, message.getInput().getText());
        assertNull(message.getContext());
    }

    @Test
    public void testRequestToMessage_NullContext() {
        WatsonMessage message = watsonMapper.requestToMessage(INPUT_TEXT, null);

        assertEquals(INPUT_TEXT, message.getInput().getText());
        assertNull(message.getContext());
    }

    @Test
    public void testRequestToMessage_WithInvalidContext() {
        WatsonMessage message = watsonMapper.requestToMessage(INPUT_TEXT, "invalidContext");

        assertEquals(INPUT_TEXT, message.getInput().getText());
        assertNull(message.getContext());
    }

    @Test
    public void testRequestToMessage_WithContext() throws IOException {
        String context = testUtil.getFileContents(CONTEXT_FILE);
        WatsonMessage message = watsonMapper.requestToMessage(INPUT_TEXT, context);
        isMessageAsExpected(message);
    }

    @Test
    public void testHttpServletRequestToMessage() throws IOException {
        String context = testUtil.getFileContents(CONTEXT_FILE);

        when(mockHttpServletRequest.getParameter("input")).thenReturn(INPUT_TEXT);
        when(mockHttpServletRequest.getParameter("context")).thenReturn(context);

        WatsonMessage message = watsonMapper.requestToMessage(mockHttpServletRequest);
        isMessageAsExpected(message);
    }

    private void isMessageAsExpected(WatsonMessage message) {
        assertEquals(INPUT_TEXT, message.getInput().getText());
        assertEquals("a96ec62f-773c-4e84-8be9-f9dbca9f83d0", message.getContext().get("conversation_id").asText());
        assertEquals("completed", message.getContext().get("system").get("branch_exited_reason").asText());
    }

    @Test
    public void testMessageToJSON_WithoutContext() throws JsonProcessingException {
        WatsonMessage message = watsonMapper.requestToMessage(INPUT_TEXT, null);
        String json = watsonMapper.toJSON(message);

        JSONObject input = new JSONObject().put("text", INPUT_TEXT);
        JSONObject expectedJSON = new JSONObject().put("input", input);

        assertEquals(expectedJSON.toString(), json);
    }

    @Test
    public void testMessageToJSON() throws IOException {
        String context = testUtil.getFileContents(CONTEXT_FILE);
        WatsonMessage message = watsonMapper.requestToMessage(INPUT_TEXT, context);
        String jsonString = watsonMapper.toJSON(message);

        JSONObject json = new JSONObject(jsonString);

        assertEquals(INPUT_TEXT, ((JSONObject) json.get("input")).get("text"));
        assertEquals("a96ec62f-773c-4e84-8be9-f9dbca9f83d0", ((JSONObject) json.get("context")).get("conversation_id"));
        assertEquals("completed", ((JSONObject) ((JSONObject) json.get("context")).get("system")).get("branch_exited_reason"));
    }

    @Test
    public void testJsonToReply() throws IOException {
        String jsonString = testUtil.getFileContents(REPLY_FILE);
        WatsonReply reply = watsonMapper.jsonToReply(jsonString);

        assertEquals("Hi there...", reply.getOutput().getText().get(0));
        assertEquals("3ca96aec-d369-4779-959c-697f333be670", reply.getContext().get("conversation_id").asText());
        assertEquals("completed", reply.getContext().get("system").get("branch_exited_reason").asText());
    }
}
