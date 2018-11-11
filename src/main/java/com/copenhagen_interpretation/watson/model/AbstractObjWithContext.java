package com.copenhagen_interpretation.watson.model;

import com.fasterxml.jackson.databind.JsonNode;

abstract class AbstractObjWithContext {
    private JsonNode context;

    public JsonNode getContext() {
        return context;
    }

    public void setContext(JsonNode context) {
        this.context = context;
    }
}
