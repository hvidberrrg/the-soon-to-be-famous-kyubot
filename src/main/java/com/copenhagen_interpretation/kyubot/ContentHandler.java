package com.copenhagen_interpretation.kyubot;

import com.copenhagen_interpretation.content.ContentMapper;
import com.copenhagen_interpretation.content.ContentUtil;
import com.copenhagen_interpretation.content.Freemarker;
import com.copenhagen_interpretation.content.model.HtmlPage;
import com.google.inject.Inject;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.util.logging.Logger;

public class ContentHandler extends HttpServlet {
    @Inject
    private static ContentMapper contentMapper;
    @Inject
    private static ContentUtil contentUtil;
    @Inject
    private static Freemarker freemarker;
    @Inject
    private static Logger logger;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String responseString;
        Template htmlTemplate = freemarker.getTemplate("kyudo.ftlh");
        String url = contentUtil.normalizeUrl(request.getRequestURI());

        if (htmlTemplate == null) {
            responseString = errorResponse(response, HttpURLConnection.HTTP_INTERNAL_ERROR);
        } else {
            if (contentUtil.isValidUrl(url)) {
                HtmlPage htmlPage = contentMapper.getHtmlPageFromURL(url);
                if (htmlPage == null) {
                    responseString = errorResponse(response, HttpURLConnection.HTTP_NOT_FOUND);
                } else {
                    Writer writer = new StringWriter();
                    try {
                        htmlTemplate.process(htmlPage, writer);
                        responseString = writer.toString();
                        response.setStatus(HttpURLConnection.HTTP_OK);
                    } catch (IOException | TemplateException e) {
                        responseString = errorResponse(response, HttpURLConnection.HTTP_INTERNAL_ERROR);
                    }
                }
            } else {
                responseString = errorResponse(response, HttpURLConnection.HTTP_UNSUPPORTED_TYPE);
            }
        }

        try {
            response.getWriter().println(responseString);
        } catch (IOException e) {
            response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
            logger.severe("Couldn't write response. The exception is: " + e);
        }
    }

    private String errorResponse(HttpServletResponse response, int statusCode) {
        response.setStatus(statusCode);
        return "<h1>Error "+ statusCode + "</h1>";
    }
}
