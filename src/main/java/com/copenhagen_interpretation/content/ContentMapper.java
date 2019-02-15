package com.copenhagen_interpretation.content;

import com.copenhagen_interpretation.content.model.HtmlPage;
import com.copenhagen_interpretation.util.GcsUtil;
import com.google.inject.Inject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.util.logging.Logger;

public class ContentMapper {
    @Inject
    private GcsUtil gcsUtil;

    @Inject
    private Logger logger;

    private JAXBContext jaxbContext;
    private Unmarshaller unmarshaller;

    ContentMapper() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(HtmlPage.class);
        unmarshaller = jaxbContext.createUnmarshaller();
    }

    public HtmlPage getHtmlPageFromURL(String url) {
        try {
            return (HtmlPage) unmarshaller.unmarshal(gcsUtil.readContent(url));
        } catch (IOException | JAXBException e) {
            logger.severe("Could not unmarshall url " + url + ". The exception is: " + e);
            return null;
        }
    }
}
