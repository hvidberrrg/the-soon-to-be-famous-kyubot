package com.copenhagen_interpretation.watson;

import com.copenhagen_interpretation.util.TestUtil;
import com.copenhagen_interpretation.watson.model.WatsonMessage;
import com.copenhagen_interpretation.watson.model.WatsonReply;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class WatsonMapperTest {
    private static String CONTEXT_FILE = "/com/copenhagen_interpretation/watson/watsonMapperTest/context.json";
    private static String REPLY_FILE = "/com/copenhagen_interpretation/watson/watsonMapperTest/watsonReply.json";
    private static String INPUT_TEXT = "Input text";
    private static TestUtil UTIL = new TestUtil();

    @Test
    public void testRequestToQuery_EmptyContext() {
        WatsonMessage message = WatsonMapper.requestToMessage(INPUT_TEXT, "");

        assertEquals(INPUT_TEXT, message.getInput().getText());
        assertEquals(null, message.getContext());
    }

    @Test
    public void testRequestToQuery_NullContext() {
        WatsonMessage message = WatsonMapper.requestToMessage(INPUT_TEXT, null);

        assertEquals(INPUT_TEXT, message.getInput().getText());
        assertEquals(null, message.getContext());
    }

    @Test
    public void testRequestToQuery_WithInvalidContext() {
        WatsonMessage message = WatsonMapper.requestToMessage(INPUT_TEXT, "invalidContext");

        assertEquals(INPUT_TEXT, message.getInput().getText());
        assertEquals(null, message.getContext());
    }

    @Test
    public void testRequestToQuery_WithContext() throws IOException {
        String context = UTIL.getFileContents(CONTEXT_FILE);
        WatsonMessage message = WatsonMapper.requestToMessage(INPUT_TEXT, context);

        assertEquals(INPUT_TEXT, message.getInput().getText());
        assertEquals("a96ec62f-773c-4e84-8be9-f9dbca9f83d0", message.getContext().get("conversation_id").asText());
        assertEquals("completed", message.getContext().get("system").get("branch_exited_reason").asText());
    }

    @Test
    public void testMessageToJSON_WithoutContext() throws JsonProcessingException {
        WatsonMessage message = WatsonMapper.requestToMessage(INPUT_TEXT, null);
        String json = WatsonMapper.toJSON(message);

        JSONObject input = new JSONObject().put("text", INPUT_TEXT);
        JSONObject expectedJSON = new JSONObject().put("input", input);

        assertEquals(expectedJSON.toString(), json);
    }

    @Test
    public void testMessageToJSON() throws IOException {
        String context = UTIL.getFileContents(CONTEXT_FILE);
        WatsonMessage message = WatsonMapper.requestToMessage(INPUT_TEXT, context);
        String jsonString = WatsonMapper.toJSON(message);

        JSONObject json = new JSONObject(jsonString);

        assertEquals(INPUT_TEXT, ((JSONObject) json.get("input")).get("text"));
        assertEquals("a96ec62f-773c-4e84-8be9-f9dbca9f83d0", ((JSONObject) json.get("context")).get("conversation_id"));
        assertEquals("completed", ((JSONObject) ((JSONObject) json.get("context")).get("system")).get("branch_exited_reason"));
    }

    @Test
    public void testJsonToReply() throws IOException {
        String jsonString = UTIL.getFileContents(REPLY_FILE);
        WatsonReply reply = WatsonMapper.jsonToReply(jsonString);

        assertEquals("Hi there...", reply.getOutput().getText().get(0));
        assertEquals("3ca96aec-d369-4779-959c-697f333be670", reply.getContext().get("conversation_id").asText());
        assertEquals("completed", reply.getContext().get("system").get("branch_exited_reason").asText());
    }
}
