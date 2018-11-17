package com.copenhagen_interpretation.watson.model;

public class WatsonMessage extends AbstractObjWithContext {
    private Input input;

    public WatsonMessage(Input input) {
        this.input = input;
    }

    public Input getInput() {
        return input;
    }
}