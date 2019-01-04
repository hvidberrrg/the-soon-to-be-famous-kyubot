package com.copenhagen_interpretation.content;

public enum SupportedContentTypes {
    HTML(".html");

    private String extension;

    SupportedContentTypes(String extension) {
        this.extension = extension;
    }

    public String extension() {
        return extension;
    }
}
