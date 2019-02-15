package com.copenhagen_interpretation.content;

public class ContentUtil {

    public boolean isValidUrl(String url) {
        boolean valid = false;
        for (SupportedContentTypes contentType : SupportedContentTypes.values()) {
            if (url.endsWith(contentType.extension())) {
                valid = true;
                break;
            }
        }

        return valid;
    }

    public String normalizeUrl(String url) {
        String lastPartOfUrl = url.substring(url.lastIndexOf('/') + 1);
        if ("".equals(lastPartOfUrl)) {
            // The url had an '/' at the last position
            url = url + "index.html";
        } else if (url.equals(lastPartOfUrl)) {
            // The url didn't contain any '/'
            url = url + "/index.html";
        } else {
            if (!lastPartOfUrl.contains(".")) {
                // The last part of the url doesn't contain a '.' and is assumed to be a directory
                url = url + "/index.html";
            } else {
                String fileExtension = url.substring(url.lastIndexOf('.') + 1);
                if ("htm".equals(fileExtension)) {
                    url = url + "l";
                }
            }
        }

        return url;
    }
}
