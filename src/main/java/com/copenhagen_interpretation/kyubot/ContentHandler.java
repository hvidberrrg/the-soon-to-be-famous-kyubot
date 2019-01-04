package com.copenhagen_interpretation.kyubot;

import com.copenhagen_interpretation.content.ContentHandlerUtil;
import com.google.inject.Inject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class ContentHandler extends HttpServlet {
    @Inject
    private static ContentHandlerUtil contentHandlerUtil;

    @Inject
    private static Logger logger;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String responseString;
        if (contentHandlerUtil.isValidUrl(request.getRequestURI())) {
            responseString = "Normalized URL: " + contentHandlerUtil.normalizeUrl(request.getRequestURI());
            response.setStatus(HttpURLConnection.HTTP_OK);
        } else {
            responseString = "<h1>404</h1>";
            response.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
        }

        try {
            response.getWriter().println(responseString);
        } catch (IOException e) {
            response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
            logger.severe("Couldn't write response. The exception is: " + e);
        }
    }
}
