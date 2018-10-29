package com.copenhagen_interpretation.watson.model;

import com.copenhagen_interpretation.watson.WatsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

abstract class AbstractObjWithContext {
    private JsonNode context;

    public JsonNode getContext() {
        return context;
    }

    public void setContext(JsonNode context) {
        this.context = context;
    }

    public String toJSON() throws JsonProcessingException {
        return WatsonMapper.toJSON(this);
    }
}
